namespace contact.Models
{
    public class UtilisateurContact
    {
        public required string eMail { get; set; }

        public List<string>? Contacts { get; set; }

        public List<Conversation>? Conversation { get; set; }
    }
}
