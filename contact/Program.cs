using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using contact.Services;
using contact.Config;

namespace contact
{
    public class Program
    {
        public static void Main(string[] args)
        {
            WebApplicationBuilder builder = WebApplication.CreateBuilder(args);
            builder.Services.AddSingleton<IContactService, ContactService>(); // Enregistrement de l'implémentation de IContactService
            builder.Services.AddControllers();

            WebApplication app = builder.Build();

            // Enregistrement du service auprès de Consul
            ConsulServiceRegistration consulServiceRegistration = new ConsulServiceRegistration(app.Configuration);
            consulServiceRegistration.RegisterServiceAsync("contact", 8084).Wait();

            // Configure the HTTP request pipeline.
            if (app.Environment.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseHttpsRedirection();
            app.UseRouting();
            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });

            app.Run();
        }
    }
}