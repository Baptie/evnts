using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    internal class ContactNonExistant : Exception
    {
        public ContactNonExistant()
        {
        }

        public ContactNonExistant(string? message) : base(message)
        {
        }

        public ContactNonExistant(string? message, Exception? innerException) : base(message, innerException)
        {
        }

        protected ContactNonExistant(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }
    }
}