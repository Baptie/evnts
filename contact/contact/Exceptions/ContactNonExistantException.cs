using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    internal class ContactNonExistantException : Exception
    {
        public ContactNonExistantException()
        {
        }

        public ContactNonExistantException(string? message) : base(message)
        {
        }

        public ContactNonExistantException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

    }
}