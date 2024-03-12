using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    internal class ConversationDejaExistanteException : Exception
    {
        public ConversationDejaExistanteException()
        {
        }

        public ConversationDejaExistanteException(string? message) : base(message)
        {
        }

        public ConversationDejaExistanteException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

    }
}