package com.example.springcybersource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringCybersourceApplication implements CommandLineRunner {

	private static boolean DEBUG = true ;

    @Value("${cybersource.apihost}") private String apihost ;
    @Value("${cybersource.merchantkeyid}") private String merchantkeyid ;
    @Value("${cybersource.merchantsecretkey}") private String merchantsecretkey ;
    @Value("${cybersource.merchantid}") private String merchantid ;

	public static void main(String[] args) {
		SpringApplication.run(SpringCybersourceApplication.class, args);
	}

    @Override
    public void run(String... args) {
		
        System.out.println("===== CYBERSOURCE PAYMENT TEST =====") ;

        if (DEBUG) {
         	System.out.println( apihost ) ;
        	System.out.println( merchantkeyid ) ;
        	System.out.println( merchantsecretkey ) ;
        	System.out.println( merchantid ) ;         	
        }
 
		CyberSourceAPI api = new CyberSourceAPI() ;
		CyberSourceAPI.setHost( apihost ) ;
		CyberSourceAPI.setKey( merchantkeyid ) ;
		CyberSourceAPI.setSecret(merchantsecretkey ) ;
		CyberSourceAPI.setMerchant( merchantid ) ;

		AuthRequest auth = new AuthRequest() ;
		auth.reference = "Order Number: 12345" ;
		auth.billToFirstName = "John" ;
		auth.billToLastName = "Doe" ;
		auth.billToAddress = "123 N. Main St." ;
		auth.billToCity = "New York" ;
		auth.billToState = "NY" ;
		auth.billToZipCode = "10001" ;
		auth.billToPhone = "(212) 123-9876" ;
		auth.billToEmail = "john.doe@email.com" ;
		auth.transactionAmount = "144.00" ;
		auth.transactionCurrency = "USD" ;
		auth.cardNumnber = "4111-1111-1111-1111" ;
		auth.cardExpMonth = "10" ;
		auth.cardExpYear = "2024" ;
		auth.cardCVV = "123" ;
		auth.cardType = "001" ;
		boolean authValid = false ;
		AuthResponse authResponse = new AuthResponse() ;
		System.out.println("\n\nAuth Request: " + auth.toJson() ) ;
		authResponse = api.authorize(auth) ;
		System.out.println("\n\nAuth Response: " + authResponse.toJson() ) ;
		if ( authResponse.status.equals("AUTHORIZED") ) {
			authValid = true ;
		}
		
		boolean captureValid = false ;
		CaptureRequest capture = new CaptureRequest() ;
		CaptureResponse captureResponse = new CaptureResponse() ;
		if ( authValid ) {
			capture.reference = "Order Number: 12345" ;
			capture.paymentId = authResponse.id ;
			capture.transactionAmount = "144.00" ;
			capture.transactionCurrency = "USD" ;
			System.out.println("\n\nCapture Request: " + capture.toJson() ) ;
			captureResponse = api.capture(capture) ;
			System.out.println("\n\nCapture Response: " + captureResponse.toJson() ) ;
			if ( captureResponse.status.equals("PENDING") ) {
				captureValid = true ;
			}

		}
	
		RefundRequest refund = new RefundRequest() ;
		RefundResponse refundResponse = new RefundResponse() ;		
		boolean refundValid = false ;

		if ( captureValid ) {
			refund.reference = "Order Number: 12345" ;
			refund.captureId = captureResponse.id ;
			refund.transactionAmount = "144.00" ;
			refund.transactionCurrency = "USD" ;
			System.out.println("\n\nRefund Request: " + refund.toJson() ) ;
			refundResponse = api.refund(refund) ;
			System.out.println("\n\nRefund Response: " + refundResponse.toJson() ) ;
			if ( refundResponse.status.equals("PENDING") ) {
				refundValid = true ;
			}
		}
	

    }	

}
