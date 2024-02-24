using System.ComponentModel.DataAnnotations;

namespace contact.Models
{
    public class UtilisateurContact
    {
        [Key]
        public required string EMail { get; set; }
        public List<string>? Contacts { get; set; }
        public List<string>? DemandeContact { get; set; }
        public List<Conversation>? Conversation { get; set; }

    }
}
