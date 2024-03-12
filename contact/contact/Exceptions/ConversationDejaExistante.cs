using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    internal class ConversationDejaExistante : Exception
    {
        public ConversationDejaExistante()
        {
        }

        public ConversationDejaExistante(string? message) : base(message)
        {
        }

        public ConversationDejaExistante(string? message, Exception? innerException) : base(message, innerException)
        {
        }

        protected ConversationDejaExistante(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }
    }
}