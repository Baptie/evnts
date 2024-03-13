using contact.Exceptions;
using contact.Models;
using contact.Services;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using NUnit.Framework;
using System.Collections.Generic;

namespace contact.Tests
{
    [TestFixture]
    public class ContactServiceTest
    {

        private ContactService service;

        [SetUp]
        public void Initialize()
        {
            service = new ContactService();
        }

        //####AjoutContact####

        [TestMethod]
        public void AjoutContact_UtilisateursExistantsEtPasDejaEnContact_AjouteContactAUtilisateurs()
        {
     
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");

            service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com");

            List<string>? contactsUtilisateur1 = service.GetContacts("utilisateur1@example.com");
            List<string>? contactsUtilisateur2 = service.GetContacts("utilisateur2@example.com");

            CollectionAssert.Contains(contactsUtilisateur1, "utilisateur2@example.com");
            CollectionAssert.Contains(contactsUtilisateur2, "utilisateur1@example.com");
        }

        [TestMethod]
        public void AjoutContact_UtilisateursExistantsEtDejaEnContact_LanceDejaEnContact()
        {
        
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com");

            Assert.ThrowsException<DejaEnContactException>(() => service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [TestMethod]
        public void AjoutContact_UtilisateurExistantEtReceveurInexistant_LanceUtilisateurNonExistant()
        {
            // Arrange
            
            service.CreationUtilisateur("utilisateur1@example.com");

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistantException>(() => service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [TestMethod]
        public void AjoutContact_UtilisateursInexistants_LanceUtilisateurNonExistant()
        {
            Assert.ThrowsException<UtilisateurNonExistantException>(() => service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }




        //####AjoutConversation####

        [TestMethod]
        public void AjoutConversation_UtilisateursExistantsEtPasDejaEnConversation_AjouteConversationEntreUtilisateurs()
        {
            // Arrange
            
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");

            // Act
            service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com");

            // Assert
            List<Conversation> conversationsUtilisateur1 = service.GetConversations("utilisateur1@example.com");
            List<Conversation> conversationsUtilisateur2 = service.GetConversations("utilisateur2@example.com");

            Assert.IsNotNull(conversationsUtilisateur1);
            Assert.IsNotNull(conversationsUtilisateur2);
            Assert.AreEqual(1, conversationsUtilisateur1.Count);
            Assert.AreEqual(1, conversationsUtilisateur2.Count);
        }

        [TestMethod]
        public void AjoutConversation_UtilisateursExistantsEtDejaEnConversation_LanceConversationDejaExistante()
        {
            // Arrange
            
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com");

            // Act & Assert
            Assert.ThrowsException<ConversationDejaExistanteException>(() => service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [TestMethod]
        public void AjoutConversation_UtilisateurExistantEtReceveurInexistant_LanceUtilisateurNonExistant()
        {
            // Arrange
            
            service.CreationUtilisateur("utilisateur1@example.com");

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistantException>(() => service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [TestMethod]
        public void AjoutConversation_UtilisateursInexistants_LanceUtilisateurNonExistant()
        {
            // Arrange
            

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistantException>(() => service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com"));
        }




        //####CreationUtilisateur####

        [TestMethod]
        public void CreationUtilisateur_UtilisateurInexistant_CreerNouvelUtilisateur()
        {
            // Arrange
            

            // Act
            service.CreationUtilisateur("utilisateur1@example.com");

            // Assert
            List<string>? contactsUtilisateur = service.GetContacts("utilisateur1@example.com");
            Assert.IsNotNull(contactsUtilisateur);
            Assert.AreEqual(0, contactsUtilisateur.Count);
        }

        [TestMethod]
        public void CreationUtilisateur_UtilisateurExistant_LanceUtilisateurDejaExistant()
        {
            // Arrange
            
            service.CreationUtilisateur("utilisateur1@example.com");

            // Act & Assert
            Assert.ThrowsException<UtilisateurDejaExistantException>(() => service.CreationUtilisateur("utilisateur1@example.com"));
        }




        //####DemandeContact####

        [TestMethod]
        public void DemandeContact_UtilisateursExistantsEtPasDejaEnDemande_AjouteDemandeEntreUtilisateurs()
        {
            // Arrange
            
            service.CreationUtilisateur("demandeur@example.com");
            service.CreationUtilisateur("receveur@example.com");

            // Act
            service.DemandeContact("demandeur@example.com", "receveur@example.com");

            // Assert
            List<string>? demandesReceveur = service.GetDemandes("receveur@example.com");
            Assert.IsNotNull(demandesReceveur);
            CollectionAssert.Contains(demandesReceveur, "demandeur@example.com");
        }

        [TestMethod]
        public void DemandeContact_UtilisateursExistantsEtDejaEnDemande_LanceDemandeDejaExistante()
        {
            // Arrange
            
            service.CreationUtilisateur("demandeur@example.com");
            service.CreationUtilisateur("receveur@example.com");
            service.DemandeContact("demandeur@example.com", "receveur@example.com");

            // Act & Assert
            Assert.ThrowsException<DemandeDejaExistanteException>(() => service.DemandeContact("demandeur@example.com", "receveur@example.com"));
        }

        [TestMethod]
        public void DemandeContact_UtilisateurExistantEtReceveurInexistant_LanceUtilisateurNonExistant()
        {
            // Arrange
            
            service.CreationUtilisateur("demandeur@example.com");

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistantException>(() => service.DemandeContact("demandeur@example.com", "receveur@example.com"));
        }

        [TestMethod]
        public void DemandeContact_UtilisateursInexistants_LanceUtilisateurNonExistant()
        {
            // Arrange
            

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistantException>(() => service.DemandeContact("demandeur@example.com", "receveur@example.com"));
        }




        //####EnvoieMessage####

        [TestMethod]
        public void EnvoieMessage_ConversationExistant_AjouteMessageALaConversation()
        {
            // Arrange
            
            service.CreationUtilisateur("envoyeur@example.com");
            service.CreationUtilisateur("receveur@example.com");
            service.AjoutConversation("envoyeur@example.com", "receveur@example.com");

            // Act
            service.EnvoieMessage("envoyeur@example.com", "receveur@example.com", "Bonjour !");

            // Assert
            Conversation conversation = service.GetConversation("envoyeur@example.com", "receveur@example.com");
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
            
            service.CreationUtilisateur("envoyeur@example.com");
            service.CreationUtilisateur("receveur@example.com");

            // Act & Assert
            Assert.ThrowsException<ConversationNonExistanteException>(() => service.EnvoieMessage("envoyeur@example.com", "receveur@example.com", "Bonjour !"));
        }




        //####GetContacts####

        [TestMethod]
        public void GetContacts_UtilisateurExistant_RetourneListeContacts()
        {
            // Arrange
            
            service.CreationUtilisateur("utilisateur1@example.com");
            service.AjoutContact("utilisateur1@example.com", "contact1@example.com");
            service.AjoutContact("utilisateur1@example.com", "contact2@example.com");

            // Act
            List<string>? contacts = service.GetContacts("utilisateur1@example.com");

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
            

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistantException>(() => service.GetContacts("utilisateur1@example.com"));
        }




        //####GetConversation####

        [TestMethod]
        public void GetConversation_ConversationExistant_RetourneConversation()
        {
            // Arrange
            
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com");

            // Act
            Conversation conversation = service.GetConversation("utilisateur1@example.com", "utilisateur2@example.com");

            // Assert
            Assert.IsNotNull(conversation);
            Assert.AreEqual("utilisateur1@example.com", conversation.UtilisateurContact1.EMail);
            Assert.AreEqual("utilisateur2@example.com", conversation.UtilisateurContact2.EMail);
        }

        [TestMethod]
        public void GetConversation_ConversationInexistante_LanceConversationNonExistante()
        {
            // Arrange
            
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");

            // Act & Assert
            Assert.ThrowsException<ConversationNonExistanteException>(() => service.GetConversation("utilisateur1@example.com", "utilisateur2@example.com"));
        }




        //####GetConversations####

        [TestMethod]
        public void GetConversations_UtilisateurExistant_RetourneListeConversations()
        {
            // Arrange
            
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com");

            // Act
            List<Conversation> conversations = service.GetConversations("utilisateur1@example.com");

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
            

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistantException>(() => service.GetConversations("utilisateur1@example.com"));
        }




        //####GetDemandes####

        [TestMethod]
        public void GetDemandes_UtilisateurExistant_RetourneListeDemandes()
        {
            // Arrange
            
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.DemandeContact("utilisateur1@example.com", "utilisateur2@example.com");

            // Act
            List<string>? demandes = service.GetDemandes("utilisateur2@example.com");

            // Assert
            Assert.IsNotNull(demandes);
            Assert.AreEqual(1, demandes.Count);
            Assert.AreEqual("utilisateur1@example.com", demandes[0]);
        }

        [TestMethod]
        public void GetDemandes_UtilisateurInexistant_LanceUtilisateurNonExistant()
        {
            // Arrange
            

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistantException>(() => service.GetDemandes("utilisateur1@example.com"));
        }




        //####SuppressionContact####

        [TestMethod]
        public void SuppressionContact_ContactExistant_SupprimeContact()
        {
            // Arrange
            
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com");

            // Act
            service.SuppressionContact("utilisateur1@example.com", "utilisateur2@example.com");

            // Assert
            List<string>? contactsUtilisateur1 = service.GetContacts("utilisateur1@example.com");
            List<string>? contactsUtilisateur2 = service.GetContacts("utilisateur2@example.com");

            Assert.IsNotNull(contactsUtilisateur1);
            Assert.IsNotNull(contactsUtilisateur2);
            Assert.AreEqual(0, contactsUtilisateur1.Count);
            Assert.AreEqual(0, contactsUtilisateur2.Count);
        }

        [TestMethod]
        public void SuppressionContact_ContactInexistant_LanceContactNonExistant()
        {
            // Arrange
            
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");

            // Act & Assert
            Assert.ThrowsException<ContactNonExistantException>(() => service.SuppressionContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [TestMethod]
        public void SuppressionContact_UtilisateurInexistant_LanceUtilisateurNonExistant()
        {
            // Arrange
            
            service.CreationUtilisateur("utilisateur1@example.com");

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistantException>(() => service.SuppressionContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }




        //####SuppressionUtilisateur####

        [TestMethod]
        public void SuppressionUtilisateur_UtilisateurExistant_SupprimeUtilisateur()
        {
            // Arrange
            
            service.CreationUtilisateur("utilisateur1@example.com");

            // Act
            service.SuppressionUtilisateur("utilisateur1@example.com");

            // Assert
            Assert.ThrowsException<UtilisateurNonExistantException>(() => service.GetContacts("utilisateur1@example.com"));
        }

        [TestMethod]
        public void SuppressionUtilisateur_UtilisateurInexistant_LanceUtilisateurNonExistant()
        {
            // Arrange
            

            // Act & Assert
            Assert.ThrowsException<UtilisateurNonExistantException>(() => service.SuppressionUtilisateur("utilisateur1@example.com"));
        }




        //####VisionDuMessage####

        [TestMethod]
        public void VisionDuMessage_MessagesNonVus_ModifieEstVu()
        {
            
            service.CreationUtilisateur("voyeur@example.com");
            service.CreationUtilisateur("envoyeur@example.com");
            service.AjoutConversation("voyeur@example.com", "envoyeur@example.com");
            service.EnvoieMessage("envoyeur@example.com", "voyeur@example.com", "Bonjour!");

            service.VisionDuMessage("voyeur@example.com", "envoyeur@example.com");

            Conversation conversation = service.GetConversation("voyeur@example.com", "envoyeur@example.com");
            foreach (Message message in conversation.Messages)
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

            
            service.CreationUtilisateur("voyeur@example.com");
            service.CreationUtilisateur("envoyeur@example.com");
            service.AjoutConversation("voyeur@example.com", "envoyeur@example.com");

            service.VisionDuMessage("voyeur@example.com", "envoyeur@example.com");

            Conversation conversation = service.GetConversation("voyeur@example.com", "envoyeur@example.com");
            foreach (Message message in conversation.Messages)
            {
                Assert.IsFalse(message.EstVu);
            }
        }
    }
}