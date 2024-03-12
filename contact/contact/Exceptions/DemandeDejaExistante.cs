using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    internal class DemandeDejaExistante : Exception
    {
        public DemandeDejaExistante()
        {
        }

        public DemandeDejaExistante(string? message) : base(message)
        {
        }

        public DemandeDejaExistante(string? message, Exception? innerException) : base(message, innerException)
        {
        }

        protected DemandeDejaExistante(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }
    }
}