namespace contact.Models
{
    public class Message
    {
        public int Id { get; set; }

        public string Contenu { get; set; }

        public DateTimeOffset DateCreation { get; set; }

        public UtilisateurContact Auteur { get; set; }

        public bool EstVu {  get; set; }


    }
}
