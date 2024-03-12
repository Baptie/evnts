﻿using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    internal class DejaEnContactException : Exception
    {
        public DejaEnContactException()
        {
        }

        public DejaEnContactException(string? message) : base(message)
        {
        }

        public DejaEnContactException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

    }
}