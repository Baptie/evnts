using System.ComponentModel.DataAnnotations;

namespace contact.Models
{
    public class Message
    {
        [Key]
        public int Id { get; set; }

        public required string Contenu { get; set; }

        public DateTimeOffset DateCreation { get; set; }

        public required UtilisateurContact Auteur { get; set; }

        public bool EstVu {  get; set; }


    }
}
