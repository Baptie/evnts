using Microsoft.AspNetCore.Mvc;
using contact.Exceptions;

namespace contact.Controllers
{
    [ApiController]
    [Route("/contact")]
    public class ContactController : ControllerBase
    {
        private readonly IContactService _contactService;

        public ContactController(IContactService contactService)
        {
            _contactService = contactService;
        }

        [HttpPost("ajout-contact")]
        public IActionResult AjoutContact(string demandeur, string receveur)
        {
            try
            {
                _contactService.AjoutContact(demandeur, receveur);
                return Ok();
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("Un des utilisateurs n'existe pas.");
            }
            catch (DejaEnContactException)
            {
                return Conflict("Les utilisateurs sont déjà en contact.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("ajout-conversation")]
        public IActionResult AjoutConversation(string utilisateur1, string utilisateur2)
        {
            try
            {
                _contactService.AjoutConversation(utilisateur1, utilisateur2);
                return Ok();
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("Un des utilisateurs n'existe pas.");
            }
            catch (ConversationDejaExistanteException)
            {
                return Conflict("La conversation existe déjà.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("creation-utilisateur")]
        public IActionResult CreationUtilisateur(string email)
        {
            try
            {
                _contactService.CreationUtilisateur(email);
                return Ok();
            }
            catch (UtilisateurDejaExistantException)
            {
                return Conflict("L'utilisateur existe déjà.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("demande-contact")]
        public IActionResult DemandeContact(string demandeur, string receveur)
        {
            try
            {
                _contactService.DemandeContact(demandeur, receveur);
                return Ok();
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'un des utilisateurs n'existe pas.");
            }
            catch (DemandeDejaExistanteException)
            {
                return Conflict("La demande de contact existe déjà.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("envoie-message")]
        public IActionResult EnvoieMessage(string envoyeur, string receveur, string contenu)
        {
            try
            {
                _contactService.EnvoieMessage(envoyeur, receveur, contenu);
                return Ok();
            }
            catch (ConversationNonExistanteException)
            {
                return NotFound("La conversation n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet("get-contacts/{utilisateur}")]
        public IActionResult GetContacts(string utilisateur)
        {
            try
            {
                var contacts = _contactService.GetContacts(utilisateur);
                return Ok(contacts);
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'utilisateur n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet("get-conversation")]
        public IActionResult GetConversation(string utilisateur1, string utilisateur2)
        {
            try
            {
                var conversation = _contactService.GetConversation(utilisateur1, utilisateur2);
                return Ok(conversation);
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'un des utilisateurs n'existe pas.");
            }
            catch (ConversationNonExistanteException)
            {
                return NotFound("La conversation n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet("get-demandes/{utilisateur}")]
        public IActionResult GetDemandes(string utilisateur)
        {
            try
            {
                var demandes = _contactService.GetDemandes(utilisateur);
                return Ok(demandes);
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'utilisateur n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpDelete("suppression-contact")]
        public IActionResult SuppressionContact(string demandeur, string cible)
        {
            try
            {
                _contactService.SuppressionContact(demandeur, cible);
                return Ok();
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'un des utilisateurs n'existe pas.");
            }
            catch (ContactNonExistantException)
            {
                return NotFound("Le contact n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpDelete("suppression-utilisateur")]
        public IActionResult SuppressionUtilisateur(string email)
        {
            try
            {
                _contactService.SuppressionUtilisateur(email);
                return Ok();
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'utilisateur n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("vision-message")]
        public IActionResult VisionDuMessage(string voyeur, string envoyeur)
        {
            try
            {
                _contactService.VisionDuMessage(voyeur, envoyeur);
                return Ok();
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'un des utilisateurs n'existe pas.");
            }
            catch (ConversationNonExistanteException)
            {
                return NotFound("La conversation n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

    }
}
