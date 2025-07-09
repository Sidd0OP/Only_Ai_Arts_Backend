package com.simulator.forum.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailSender {

	@Autowired
    private JavaMailSender emailSender;
	
	
	private final String template = """
			
									<!DOCTYPE html>
									<html lang="en">
									  <head>
									    <meta charset="UTF-8" />
									    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
									    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
									    <title>Password Reset</title>
									    <link
									      href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap"
									      rel="stylesheet"
									    />
									    <style>
									      body {
									        margin: 0;
									        font-family: 'Poppins', sans-serif;
									        background: #000000;
									        font-size: 14px;
									        color: #ffffff;
									      }
									
									      .container {
									        max-width: 680px;
									        margin: 0 auto;
									        padding: 45px 30px 60px;
									        background: #000000;
									      }
									
									      .main-box {
									        margin-top: 50px;
									        padding: 60px 30px;
									        background: #ffffff;
									        border-radius: 20px;
									        text-align: center;
									        color: #000000;
									      }
									
									      .code-box {
									        margin-top: 40px;
									        font-size: 18px;
									        font-weight: 600;
									        color: #000000;
									        background: #eaeaea;
									        padding: 12px 20px;
									        border-radius: 8px;
									        display: inline-block;
									        cursor: pointer;
									        user-select: all;
									        transition: background 0.2s ease;
									      }
									
									      .code-box:hover {
									        background: #dddddd;
									      }
									
									      .footer {
									        max-width: 490px;
									        margin: 30px auto 0;
									        text-align: center;
									        border-top: 1px solid #ffffff22;
									        padding-top: 30px;
									        color: #ffffff;
									      }
									
									      .support-link {
									        color: #ffffff;
									        text-decoration: underline;
									      }
									    </style>
									  </head>
									  <body>
									    <div class="container">
									      <header>
									        <table style="width: 100%">
									          <tr>
									            <td>
									              <img
									                alt="OnlyAiArts"
									                src="https://onlyaiarts.com/OnlyAiArtsLogo.png"
									                height="30px"
									              />
									            </td>
									          </tr>
									        </table>
									      </header>
									
									      <main>
									        <div class="main-box">
									          <div style="max-width: 480px; margin: 0 auto">
									            <h1 style="font-size: 22px; font-weight: 600">Your Password Reset Code</h1>
									            <p style="font-size: 16px; font-weight: 500">Hey,</p>
									            <p style="margin-top: 20px; font-weight: 400; line-height: 1.6">
									              Looks like you forgot your password. It happens. Below is your reset code.
									              It's valid for <strong>5 minutes</strong>. Please don't share this with anyone.
									            </p>
									            <p id="code" class="code-box" onclick="copyCode()">
									              {{code}}
									            </p>
									            <p id="copied-msg" style="margin-top: 12px; font-size: 13px; color: green; display: none;">
									              Copied to clipboard!
									            </p>
									          </div>
									        </div>
									
									        <p style="max-width: 400px; margin: 60px auto 0; text-align: center; font-weight: 500; font-size: 13px">
									          Need help? Email us at
									          <a href="mailto:support@onlyaiarts.com" class="support-link">support@onlyaiarts.com</a>
									        </p>
									      </main>
									
									      <footer class="footer">
									        <p style="margin: 0; font-size: 14px; font-weight: 600">Only Ai Arts</p>
									        <p style="margin: 8px 0 0">Made with 💗 in Noida</p>
									        <p style="margin: 20px 0 0; font-size: 12px">
									          © 2025 OnlyAiArts. All rights reserved.
									        </p>
									      </footer>
									    </div>
									
									    <script>
									      function copyCode() {
									        const codeEl = document.getElementById('code');
									        const codeText = codeEl.innerText;
									
									        navigator.clipboard.writeText(codeText).then(() => {
									          const msg = document.getElementById('copied-msg');
									          msg.style.display = 'block';
									          setTimeout(() => {
									            msg.style.display = 'none';
									          }, 2000);
									        });
									      }
									    </script>
									  </body>
									</html>

							
			
									""";
	
	public void sendSimpleMessage(String to, String subject, String text) throws MessagingException 
	{	
				MimeMessage mimeMessage = emailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
				
				String htmlMsg = template.replace("{{code}}", text);
				

				helper.setText(htmlMsg, true);
				helper.setTo(to);
				helper.setSubject(subject);
				helper.setFrom("support@onlyaiarts.com");
				emailSender.send(mimeMessage);
		        

		       
	}
}
