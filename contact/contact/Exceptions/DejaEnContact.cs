using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    internal class DejaEnContact : Exception
    {
        public DejaEnContact()
        {
        }

        public DejaEnContact(string? message) : base(message)
        {
        }

        public DejaEnContact(string? message, Exception? innerException) : base(message, innerException)
        {
        }

    }
}