using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    internal class ConversationNonExistanteException : Exception
    {
        public ConversationNonExistanteException()
        {
        }

        public ConversationNonExistanteException(string? message) : base(message)
        {
        }

        public ConversationNonExistanteException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

    }
}