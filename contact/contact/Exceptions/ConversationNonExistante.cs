using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    internal class ConversationNonExistante : Exception
    {
        public ConversationNonExistante()
        {
        }

        public ConversationNonExistante(string? message) : base(message)
        {
        }

        public ConversationNonExistante(string? message, Exception? innerException) : base(message, innerException)
        {
        }

        protected ConversationNonExistante(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }
    }
}