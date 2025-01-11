package org.ptss.support.domain.templates

open class BaseEmailTemplate(
    val to: String,
    val subject: String,
    private val content: String,
) {
    val wrappedContent: String by lazy {
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                /* Reset styles */
                body, p, h1, h2, h3, h4, h5, h6, div {
                    margin: 0;
                    padding: 0;
                }
                
                /* Base styles */
                body {
                    font-family: 'Quicksand', Arial, sans-serif;
                    line-height: 1.6;
                    color: #2D3831;
                    background-color: #ffffff;
                    margin: 0;
                    padding: 0;
                }
                
                /* Container */
                .container {
                    max-width: 600px;
                    margin: 0 auto;
                    padding: 20px;
                }
                
                /* Logo */
                .logo {
                    text-align: center;
                    padding: 20px 0;
                }
                
                .logo img {
                    width: 80px;
                    height: auto;
                }
                
                /* Content */
                .content {
                    background-color: #ffffff;
                    padding: 20px;
                    border-radius: 8px;
                }
                
                /* Typography */
                h1 {
                    font-size: 24px;
                    font-weight: 700;
                    margin-bottom: 20px;
                    color: #2D3831;
                }
                
                p {
                    font-size: 16px;
                    margin-bottom: 16px;
                    color: #2D3831;
                }
                
                /* Verification code box */
                .code-box {
                    position: relative;
                    background-color: #BFD1B7;
                    padding: 24px;
                    margin: 24px 0;
                    text-align: center;
                    border-radius: 8px;
                }
        
                .code-box strong {
                    font-size: 32px;
                    letter-spacing: 0.05em;
                    color: #2D3831;
                }
                
                /* Footer */
                .footer {
                    margin-top: 30px;
                    padding-top: 20px;
                    border-top: 1px solid #BFD1B7;
                    text-align: center;
                }
                
                .footer p {
                    font-size: 14px;
                    color: #8EAB94;
                }
                
                /* Responsive adjustments */
                @media screen and (max-width: 600px) {
                    .container {
                        padding: 10px;
                    }
                    
                    .content {
                        padding: 15px;
                    }
                    
                    h1 {
                        font-size: 20px;
                    }
                    
                    p {
                        font-size: 14px;
                    }
                    
                    .code-box strong {
                        font-size: 28px;
                    }
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="logo">
                    <img src="https://cdn.discordapp.com/attachments/1280575010531971197/1327317192139866195/image.png?ex=67829ff6&is=67814e76&hm=834342766e0f035132a99780b66c5010508fa2e3e257ceaa85b2fef1fb844e10&" 
                        alt="PTSS Kompas Logo" 
                        width="150" 
                        height="150"
                        style="width: 150px; height: 150px;">
                </div>
                <div class="content">
                    $content
                </div>
                <div class="footer">
                    <p>Dit is een automatisch gegenereerd bericht, reageer hier niet rechtstreeks op.</p>
                    <p>Als je hulp nodig hebt, neem dan contact op met ons support team.</p>
                </div>
            </div>
        </body>
        </html>
        """.trimIndent()
    }
}