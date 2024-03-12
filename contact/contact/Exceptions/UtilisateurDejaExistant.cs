using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    internal class UtilisateurDejaExistant : Exception
    {
        public UtilisateurDejaExistant()
        {
        }

        public UtilisateurDejaExistant(string? message) : base(message)
        {
        }

        public UtilisateurDejaExistant(string? message, Exception? innerException) : base(message, innerException)
        {
        }

    }
}