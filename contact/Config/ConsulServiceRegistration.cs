using Consul;
using Microsoft.Extensions.Configuration;

namespace contact.Config
{
    public class ConsulServiceRegistration
    {
        private readonly IConfiguration _configuration;

        public ConsulServiceRegistration(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public async Task RegisterServiceAsync(string serviceName, int servicePort)
        {
            var consulClient = new ConsulClient();

            var registration = new AgentServiceRegistration()
            {
                ID = Guid.NewGuid().ToString(),
                Name = serviceName,
                Address = "localhost", // Adresse de l'hôte où votre service est hébergé
                Port = servicePort
            };

            await consulClient.Agent.ServiceRegister(registration);
        }
    }
}