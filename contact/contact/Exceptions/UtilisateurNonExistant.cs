using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    internal class UtilisateurNonExistant : Exception
    {
        public UtilisateurNonExistant()
        {
        }

        public UtilisateurNonExistant(string? message) : base(message)
        {
        }

        public UtilisateurNonExistant(string? message, Exception? innerException) : base(message, innerException)
        {
        }

    }
}