using contact.Exceptions;
using contact.Services;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace contact.Tests
{
    [TestClass]
    public class ContactServiceTest
    {

        //####AjoutContact####

        [TestMethod]
        public void AjoutContact_UtilisateursExistantsEtPasDejaEnContact_AjouteContactAUtilisateurs()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");

            // Act
            service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com");

            // Assert
            var contactsUtilisateur1 = service.GetContacts("utilisateur1@example.com");
            var contactsUtilisateur2 = service.GetContacts("utilisateur2@example.com");

            CollectionAssert.Contains(contactsUtilisateur1, "utilisateur2@example.com");
            CollectionAssert.Contains(contactsUtilisateur2, "utilisateur1@example.com");
        }

        [TestMethod]
        public void AjoutContact_UtilisateursExistantsEtDejaEnContact_LanceDejaEnContact()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com");

            // Act & Assert
            Assert.ThrowsException<DejaEnContact>(() => service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [TestMethod]
        public void AjoutContact_UtilisateurExistantEtReceveurInexistant_LanceUtilisateurNonExistant()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistant>(() => service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [TestMethod]
        public void AjoutContact_UtilisateursInexistants_LanceUtilisateurNonExistant()
        {
            // Arrange
            var service = new ContactService();

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistant>(() => service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }




        //####AjoutConversation####

        [TestMethod]
        public void AjoutConversation_UtilisateursExistantsEtPasDejaEnConversation_AjouteConversationEntreUtilisateurs()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");

            // Act
            service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com");

            // Assert
            var conversationsUtilisateur1 = service.GetConversations("utilisateur1@example.com");
            var conversationsUtilisateur2 = service.GetConversations("utilisateur2@example.com");

            Assert.IsNotNull(conversationsUtilisateur1);
            Assert.IsNotNull(conversationsUtilisateur2);
            Assert.AreEqual(1, conversationsUtilisateur1.Count);
            Assert.AreEqual(1, conversationsUtilisateur2.Count);
        }

        [TestMethod]
        public void AjoutConversation_UtilisateursExistantsEtDejaEnConversation_LanceConversationDejaExistante()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com");

            // Act & Assert
            Assert.ThrowsException<ConversationDejaExistante>(() => service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [TestMethod]
        public void AjoutConversation_UtilisateurExistantEtReceveurInexistant_LanceUtilisateurNonExistant()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistant>(() => service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [TestMethod]
        public void AjoutConversation_UtilisateursInexistants_LanceUtilisateurNonExistant()
        {
            // Arrange
            var service = new ContactService();

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistant>(() => service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com"));
        }




        //####CreationUtilisateur####

        [TestMethod]
        public void CreationUtilisateur_UtilisateurInexistant_CreerNouvelUtilisateur()
        {
            // Arrange
            var service = new ContactService();

            // Act
            service.CreationUtilisateur("utilisateur1@example.com");

            // Assert
            var contactsUtilisateur = service.GetContacts("utilisateur1@example.com");
            Assert.IsNotNull(contactsUtilisateur);
            Assert.AreEqual(0, contactsUtilisateur.Count);
        }

        [TestMethod]
        public void CreationUtilisateur_UtilisateurExistant_LanceUtilisateurDejaExistant()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");

            // Act & Assert
            Assert.ThrowsException<UtilisateurDejaExistant>(() => service.CreationUtilisateur("utilisateur1@example.com"));
        }




        //####DemandeContact####

        [TestMethod]
        public void DemandeContact_UtilisateursExistantsEtPasDejaEnDemande_AjouteDemandeEntreUtilisateurs()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("demandeur@example.com");
            service.CreationUtilisateur("receveur@example.com");

            // Act
            service.DemandeContact("demandeur@example.com", "receveur@example.com");

            // Assert
            var demandesReceveur = service.GetDemandes("receveur@example.com");
            Assert.IsNotNull(demandesReceveur);
            CollectionAssert.Contains(demandesReceveur, "demandeur@example.com");
        }

        [TestMethod]
        public void DemandeContact_UtilisateursExistantsEtDejaEnDemande_LanceDemandeDejaExistante()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("demandeur@example.com");
            service.CreationUtilisateur("receveur@example.com");
            service.DemandeContact("demandeur@example.com", "receveur@example.com");

            // Act & Assert
            Assert.ThrowsException<DemandeDejaExistante>(() => service.DemandeContact("demandeur@example.com", "receveur@example.com"));
        }

        [TestMethod]
        public void DemandeContact_UtilisateurExistantEtReceveurInexistant_LanceUtilisateurNonExistant()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("demandeur@example.com");

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistant>(() => service.DemandeContact("demandeur@example.com", "receveur@example.com"));
        }

        [TestMethod]
        public void DemandeContact_UtilisateursInexistants_LanceUtilisateurNonExistant()
        {
            // Arrange
            var service = new ContactService();

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistant>(() => service.DemandeContact("demandeur@example.com", "receveur@example.com"));
        }




        //####EnvoieMessage####

        [TestMethod]
        public void EnvoieMessage_ConversationExistant_AjouteMessageALaConversation()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("envoyeur@example.com");
            service.CreationUtilisateur("receveur@example.com");
            service.AjoutConversation("envoyeur@example.com", "receveur@example.com");

            // Act
            service.EnvoieMessage("envoyeur@example.com", "receveur@example.com", "Bonjour !");

            // Assert
            var conversation = service.GetConversation("envoyeur@example.com", "receveur@example.com");
            Assert.IsNotNull(conversation.Messages);
            Assert.AreEqual(1, conversation.Messages.Count);
            Assert.AreEqual("Bonjour !", conversation.Messages[0].Contenu);
            Assert.AreEqual("envoyeur@example.com", conversation.Messages[0].Auteur);
            Assert.IsFalse(conversation.Messages[0].EstVu);
        }

        [TestMethod]
        public void EnvoieMessage_ConversationInexistante_LanceConversationNonExistante()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("envoyeur@example.com");
            service.CreationUtilisateur("receveur@example.com");

            // Act & Assert
            Assert.ThrowsException<ConversationNonExistante>(() => service.EnvoieMessage("envoyeur@example.com", "receveur@example.com", "Bonjour !"));
        }




        //####GetContacts####

        [TestMethod]
        public void GetContacts_UtilisateurExistant_RetourneListeContacts()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");
            service.AjoutContact("utilisateur1@example.com", "contact1@example.com");
            service.AjoutContact("utilisateur1@example.com", "contact2@example.com");

            // Act
            var contacts = service.GetContacts("utilisateur1@example.com");

            // Assert
            Assert.IsNotNull(contacts);
            Assert.AreEqual(2, contacts.Count);
            CollectionAssert.Contains(contacts, "contact1@example.com");
            CollectionAssert.Contains(contacts, "contact2@example.com");
        }

        [TestMethod]
        public void GetContacts_UtilisateurInexistant_LanceUtilisateurNonExistant()
        {
            // Arrange
            var service = new ContactService();

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistant>(() => service.GetContacts("utilisateur1@example.com"));
        }




        //####GetConversation####

        [TestMethod]
        public void GetConversation_ConversationExistant_RetourneConversation()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com");

            // Act
            var conversation = service.GetConversation("utilisateur1@example.com", "utilisateur2@example.com");

            // Assert
            Assert.IsNotNull(conversation);
            Assert.AreEqual("utilisateur1@example.com", conversation.UtilisateurContact1.EMail);
            Assert.AreEqual("utilisateur2@example.com", conversation.UtilisateurContact2.EMail);
        }

        [TestMethod]
        public void GetConversation_ConversationInexistante_LanceConversationNonExistante()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");

            // Act & Assert
            Assert.ThrowsException<ConversationNonExistante>(() => service.GetConversation("utilisateur1@example.com", "utilisateur2@example.com"));
        }




        //####GetConversations####

        [TestMethod]
        public void GetConversations_UtilisateurExistant_RetourneListeConversations()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com");

            // Act
            var conversations = service.GetConversations("utilisateur1@example.com");

            // Assert
            Assert.IsNotNull(conversations);
            Assert.AreEqual(1, conversations.Count);
            Assert.AreEqual("utilisateur1@example.com", conversations[0].UtilisateurContact1.EMail);
            Assert.AreEqual("utilisateur2@example.com", conversations[0].UtilisateurContact2.EMail);
        }

        [TestMethod]
        public void GetConversations_UtilisateurInexistant_LanceUtilisateurNonExistant()
        {
            // Arrange
            var service = new ContactService();

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistant>(() => service.GetConversations("utilisateur1@example.com"));
        }




        //####GetDemandes####

        [TestMethod]
        public void GetDemandes_UtilisateurExistant_RetourneListeDemandes()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.DemandeContact("utilisateur1@example.com", "utilisateur2@example.com");

            // Act
            var demandes = service.GetDemandes("utilisateur2@example.com");

            // Assert
            Assert.IsNotNull(demandes);
            Assert.AreEqual(1, demandes.Count);
            Assert.AreEqual("utilisateur1@example.com", demandes[0]);
        }

        [TestMethod]
        public void GetDemandes_UtilisateurInexistant_LanceUtilisateurNonExistant()
        {
            // Arrange
            var service = new ContactService();

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistant>(() => service.GetDemandes("utilisateur1@example.com"));
        }




        //####SuppressionContact####

        [TestMethod]
        public void SuppressionContact_ContactExistant_SupprimeContact()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com");

            // Act
            service.SuppressionContact("utilisateur1@example.com", "utilisateur2@example.com");

            // Assert
            var contactsUtilisateur1 = service.GetContacts("utilisateur1@example.com");
            var contactsUtilisateur2 = service.GetContacts("utilisateur2@example.com");

            Assert.IsNotNull(contactsUtilisateur1);
            Assert.IsNotNull(contactsUtilisateur2);
            Assert.AreEqual(0, contactsUtilisateur1.Count);
            Assert.AreEqual(0, contactsUtilisateur2.Count);
        }

        [TestMethod]
        public void SuppressionContact_ContactInexistant_LanceContactNonExistant()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");

            // Act & Assert
            Assert.ThrowsException<ContactNonExistant>(() => service.SuppressionContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [TestMethod]
        public void SuppressionContact_UtilisateurInexistant_LanceUtilisateurNonExistant()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistant>(() => service.SuppressionContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }




        //####SuppressionUtilisateur####

        [TestMethod]
        public void SuppressionUtilisateur_UtilisateurExistant_SupprimeUtilisateur()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("utilisateur1@example.com");

            // Act
            service.SuppressionUtilisateur("utilisateur1@example.com");

            // Assert
            Assert.ThrowsException<UtilisateurNonExistant>(() => service.GetContacts("utilisateur1@example.com"));
        }

        [TestMethod]
        public void SuppressionUtilisateur_UtilisateurInexistant_LanceUtilisateurNonExistant()
        {
            // Arrange
            var service = new ContactService();

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistant>(() => service.SuppressionUtilisateur("utilisateur1@example.com"));
        }




        //####VisionDuMessage####

        [TestMethod]
        public void VisionDuMessage_MessagesNonVus_ModifieEstVu()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("voyeur@example.com");
            service.CreationUtilisateur("envoyeur@example.com");
            service.AjoutConversation("voyeur@example.com", "envoyeur@example.com");
            service.EnvoieMessage("envoyeur@example.com", "voyeur@example.com", "Bonjour!");

            // Act
            service.VisionDuMessage("voyeur@example.com", "envoyeur@example.com");

            // Assert
            var conversation = service.GetConversation("voyeur@example.com", "envoyeur@example.com");
            foreach (var message in conversation.Messages)
            {
                if (message.Auteur != "voyeur@example.com")
                {
                    Assert.IsTrue(message.EstVu);
                }
            }
        }

        [TestMethod]
        public void VisionDuMessage_AucunMessage_ModificationNonNecessaire()
        {
            // Arrange
            var service = new ContactService();
            service.CreationUtilisateur("voyeur@example.com");
            service.CreationUtilisateur("envoyeur@example.com");
            service.AjoutConversation("voyeur@example.com", "envoyeur@example.com");

            // Act
            service.VisionDuMessage("voyeur@example.com", "envoyeur@example.com");

            // Assert
            var conversation = service.GetConversation("voyeur@example.com", "envoyeur@example.com");
            foreach (var message in conversation.Messages)
            {
                Assert.IsFalse(message.EstVu);
            }
        }
    }
}