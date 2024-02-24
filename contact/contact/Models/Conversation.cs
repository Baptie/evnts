using System.ComponentModel.DataAnnotations;

namespace contact.Models
{
    public class Conversation
    {

        [Key]
        public required int Id { get; set; }

        public required UtilisateurContact UtilisateurContact1 { get; set; }

        public required UtilisateurContact UtilisateurContact2 { get; set; }

        public List<Message>? Messages { get; set; }
    }
}
