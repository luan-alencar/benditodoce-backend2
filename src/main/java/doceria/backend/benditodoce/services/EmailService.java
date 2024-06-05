package doceria.backend.benditodoce.services;

import doceria.backend.benditodoce.domain.Cliente;
import doceria.backend.benditodoce.domain.Pedido;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendNewPasswordEmail(Cliente cliente, String newPass);
}
