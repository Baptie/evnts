using contact.Exceptions;
using contact.Models;

namespace contact.Services
{
    public class ContactService : IContactService
    {
        private static List<UtilisateurContact> _utilisateurList = new List<UtilisateurContact>();

        public void AjoutContact(string Demandeur, string Receveur)
        {
            // Vérifier si les deux utilisateurs existent dans la liste des utilisateurs
            UtilisateurContact? utilisateur1 = _utilisateurList.SingleOrDefault(u => u.EMail == Demandeur);
            UtilisateurContact? utilisateur2 = _utilisateurList.SingleOrDefault(u => u.EMail == Receveur);

            if (utilisateur1 == null || utilisateur2 == null)
                throw new UtilisateurNonExistant();

            if (utilisateur1.Contacts.Contains(Receveur) && utilisateur2.Contacts.Contains(Demandeur))
                throw new DejaEnContact();

            utilisateur2.Contacts.Add(Demandeur);
            utilisateur1.Contacts.Add(Receveur);
        }

        public void AjoutConversation(string Utilisateur1, string Utilisateur2)
        {
            //classe par ordre alphabétique pour éviter doublon de conversation
            if (String.Compare(Utilisateur1, Utilisateur2, StringComparison.Ordinal) > 0)
            {
                string temp = Utilisateur1;
                Utilisateur1 = Utilisateur2;
                Utilisateur2 = temp;
            }

            int id = GetIdConversation(Utilisateur1, Utilisateur2);

            // Vérifier si les deux utilisateurs existent dans la liste des utilisateurs
            UtilisateurContact? utilisateur1 = _utilisateurList.SingleOrDefault(u => u.EMail == Utilisateur1);
            UtilisateurContact? utilisateur2 = _utilisateurList.SingleOrDefault(u => u.EMail == Utilisateur2);

            if (utilisateur1 == null || utilisateur2 == null)
                throw new UtilisateurNonExistant();

            if (utilisateur1.Conversation != null && utilisateur2.Conversation != null)
            {

                var conversationExistante = utilisateur1.Conversation.FirstOrDefault(u => u.Id == id);

                if (conversationExistante != null)
                    throw new ConversationDejaExistante();
            }
            

            var nouvelleConversation = new Conversation
            {
                Id=id,
                UtilisateurContact1 = utilisateur1,
                UtilisateurContact2 = utilisateur2,
                Messages = new List<Message>()
            };

            utilisateur1.Conversation.Add(nouvelleConversation);
            utilisateur2.Conversation.Add(nouvelleConversation);

        }

        public void CreationUtilisateur(string EMail)
        {
            UtilisateurContact? utilisateur = _utilisateurList.SingleOrDefault(u => u.EMail == EMail);

            if (utilisateur != null)
                throw new UtilisateurDejaExistant();

            utilisateur = new UtilisateurContact()
            {
                EMail = EMail
            };

            _utilisateurList.Add(utilisateur);
        }

        public void DemandeContact(string Demandeur, string Receveur)
        {

            UtilisateurContact? utilisateur1 = _utilisateurList.SingleOrDefault(u => u.EMail == Demandeur);
            UtilisateurContact? utilisateur2 = _utilisateurList.SingleOrDefault(u => u.EMail == Receveur);

            if (utilisateur1 == null || utilisateur2 == null)
                throw new UtilisateurNonExistant();

            if (utilisateur2.DemandeContact.SingleOrDefault(u => u.Equals(Demandeur)) != null)
            {
                throw new DemandeDejaExistante();
            }

            utilisateur2.DemandeContact.Add(Receveur);

        }

        public void EnvoieMessage(string Envoyeur, string Receveur, string Contenu)
        {
            Conversation conversation = GetConversation(Envoyeur, Receveur);
            List<Message> messages = conversation.Messages;
            
            var nouveauMessage = new Message
            {
                Id = messages.Count,
                Contenu = Contenu,
                DateCreation = DateTime.Now,
                Auteur = Envoyeur,
                EstVu = false 
            };

            messages.Add(nouveauMessage);
        }

        public List<string>? GetContacts(string Utilisateur)
        {
            UtilisateurContact? utilisateur = _utilisateurList.SingleOrDefault(u => u.EMail == Utilisateur);

            if (utilisateur == null)
                throw new UtilisateurNonExistant();

            return utilisateur.Contacts;
        }

        public Conversation GetConversation(string Utilisateur1, string Utilisateur2)
        {
            //classe par ordre alphabétique pour éviter doublon de conversation
            if (String.Compare(Utilisateur1, Utilisateur2, StringComparison.Ordinal) > 0)
            {
                string temp = Utilisateur1;
                Utilisateur1 = Utilisateur2;
                Utilisateur2 = temp;
            }

            int id = GetIdConversation(Utilisateur1,Utilisateur2);

            UtilisateurContact? utilisateur = _utilisateurList.SingleOrDefault(u => u.EMail == Utilisateur1);

            if (utilisateur != null)
                throw new UtilisateurDejaExistant();

            Conversation conversation = utilisateur.Conversation.FirstOrDefault(c => c.Id == id);
            
            if (utilisateur == null)
                throw new ConversationNonExistante();

            return conversation;

        }

        public List<Conversation>? GetConversations(string Utilisateur)
        {
            UtilisateurContact? utilisateur = _utilisateurList.SingleOrDefault(u => u.EMail == Utilisateur);

            if (utilisateur == null)
                throw new UtilisateurNonExistant();

            return utilisateur.Conversation;
        }

        public List<string>? GetDemandes(string Utilisateur)
        {
            UtilisateurContact? utilisateur = _utilisateurList.SingleOrDefault(u => u.EMail == Utilisateur);

            if (utilisateur == null)
                throw new UtilisateurNonExistant();

            return utilisateur.DemandeContact;
        }

        public void SuppressionContact(string Demandeur, string Cible)
        {
            UtilisateurContact? utilisateur1 = _utilisateurList.SingleOrDefault(u => u.EMail == Demandeur);
            UtilisateurContact? utilisateur2 = _utilisateurList.SingleOrDefault(u => u.EMail == Cible);

            if (utilisateur1 == null || utilisateur2 == null)
                throw new UtilisateurNonExistant();

            if (!utilisateur1.Contacts.Contains(Cible))
                throw new ContactNonExistant();

            utilisateur1.Contacts.Remove(Cible);
            utilisateur2.Contacts.Remove(Demandeur);
        }

        public void SuppressionUtilisateur(string EMail)
        {
            UtilisateurContact? utilisateur = _utilisateurList.SingleOrDefault(u => u.EMail == EMail);
            
            if (utilisateur == null)
                throw new UtilisateurNonExistant();

            _utilisateurList.Remove(utilisateur);
        }

        public void VisionDuMessage(string Voyeur,string Envoyeur)
        {
            Conversation conversation = GetConversation(Voyeur, Envoyeur);

            foreach (Message message in conversation.Messages)
            {
                if (message.Auteur != Voyeur && message.EstVu == false)
                {
                    message.EstVu = true;
                }
            }
        }

        private int GetIdConversation(string Utilisateur1, string Utilisateur2)
        {
            int hash = (Utilisateur1 + Utilisateur2).GetHashCode();
            return Math.Abs(hash);
        }
    }
}
