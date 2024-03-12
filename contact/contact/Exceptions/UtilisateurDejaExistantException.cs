using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    internal class UtilisateurDejaExistantException : Exception
    {
        public UtilisateurDejaExistantException()
        {
        }

        public UtilisateurDejaExistantException(string? message) : base(message)
        {
        }

        public UtilisateurDejaExistantException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

    }
}