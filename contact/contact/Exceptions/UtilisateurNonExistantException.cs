using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    internal class UtilisateurNonExistantException : Exception
    {
        public UtilisateurNonExistantException()
        {
        }

        public UtilisateurNonExistantException(string? message) : base(message)
        {
        }

        public UtilisateurNonExistantException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

    }
}