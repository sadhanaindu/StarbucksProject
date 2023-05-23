package com.example.springpayments;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import lombok.Getter;
import lombok.Setter;

import com.example.springcybersource.* ;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Controller
@RequestMapping("/")
public class PaymentsController {

    @Value("${cybersource.apihost}") private String apihost ;
    @Value("${cybersource.merchantkeyid}") private String merchantkeyid ;
    @Value("${cybersource.merchantsecretkey}") private String merchantsecretkey ;
    @Value("${cybersource.merchantid}") private String merchantid ;


    private CyberSourceAPI api = new CyberSourceAPI() ;

    private static Map<String,String> months = new HashMap<>() ;
    static {
        months.put( "January", "01" ) ;
        months.put( "February", "02" ) ;
        months.put( "March", "03" ) ;
        months.put( "April", "04" ) ;
        months.put( "May", "05" ) ;
        months.put( "June", "06" ) ;
        months.put( "July", "07" ) ;
        months.put( "August", "08" ) ;
        months.put( "September", "09" ) ;
        months.put( "October", "10" ) ;
        months.put( "November", "11" ) ;
        months.put( "December", "12" ) ;
    }

    private static Map<String,String> states = new HashMap<>() ;
    static {
        states.put( "AL", "Alabama" ) ;
        states.put( "AK", "Alaska" ) ;
        states.put( "AZ", "Arizona" ) ;
        states.put( "AR", "Arkansas" ) ;
        states.put( "CA", "California" ) ;
        states.put( "CO", "Colorado" ) ;
        states.put( "CT", "Connecticut" ) ;
        states.put( "DE", "Delaware" ) ;
        states.put( "FL", "Florida" ) ;
        states.put( "GA", "Georgia" ) ;
        states.put( "HI", "Hawaii" ) ;
        states.put( "ID", "Idaho" ) ;
        states.put( "IL", "Illinois" ) ;
        states.put( "IN", "Indiana" ) ;
        states.put( "IA", "Iowa" ) ;
        states.put( "KS", "Kansas" ) ;
        states.put( "KY", "Kentucky" ) ;
        states.put( "LA", "Louisiana" ) ;
        states.put( "ME", "Maine" ) ;
        states.put( "MD", "Maryland" ) ;
        states.put( "MA", "Massachusetts" ) ;
        states.put( "MI", "Michigan" ) ;
        states.put( "MN", "Minnesota" ) ;
        states.put( "MS", "Mississippi" ) ;
        states.put( "MO", "Missouri" ) ;
        states.put( "MT", "Montana" ) ;
        states.put( "NE", "Nebraska" ) ;
        states.put( "NV", "Nevada" ) ;
        states.put( "NH", "New Hampshire" ) ;
        states.put( "NJ", "New Jersey" ) ;
        states.put( "NM", "New Mexico" ) ;
        states.put( "NY", "New York" ) ;
        states.put( "NC", "North Carolina" ) ;
        states.put( "ND", "North Dakota" ) ;
        states.put( "OH", "Ohio" ) ;
        states.put( "OK", "Oklahoma" ) ;
        states.put( "OR", "Oregon" ) ;
        states.put( "PA", "Pennsylvania" ) ;
        states.put( "RI", "Rhode Island" ) ;
        states.put( "SC", "South Carolina" ) ;
        states.put( "SD", "South Dakota" ) ;
        states.put( "TN", "Tennessee" ) ;
        states.put( "TX", "Texas" ) ;
        states.put( "UT", "Utah" ) ;
        states.put( "VT", "Vermont" ) ;
        states.put( "VA", "Virginia" ) ;
        states.put( "WA", "Washington" ) ;
        states.put( "WV", "West Virginia" ) ;
        states.put( "WI", "Wisconsin" ) ;
        states.put( "WY", "Wyoming"  ) ;
    }

    @Autowired
    private PaymentsCommandRepository repository ;

    @GetMapping
    public String getAction( @ModelAttribute("command") PaymentsCommand command,
                             Model model) {

        /* Render View */
        return "creditcards" ;

    }

    class Message {
        private String message ;
        public Message(String m)
        {
            message = m ;
        }

        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
    }

    @PostMapping
    public String postAction(@Valid @ModelAttribute("command") PaymentsCommand command,
                             @RequestParam(value="action", required=true) String action,
                             Errors errors, Model model, HttpServletRequest request) {

        log.info( "Action: " + action ) ;
        log.info( "Command: " + command ) ;

        CyberSourceAPI.setHost( apihost) ;
        CyberSourceAPI.setKey( merchantkeyid ) ;
        CyberSourceAPI.setSecret(merchantsecretkey ) ;
        CyberSourceAPI.setMerchant( merchantid ) ;

        CyberSourceAPI.debugConfig() ;

        ArrayList<Message> moreMessages = new ArrayList<Message>();

        /* check for errors */
        boolean hasErrors = false ;
        if ( command.firstname().equals("") )   { hasErrors = true ; moreMessages.add( new Message("First Name Required." )) ; }
        if ( command.lastname().equals("") )    { hasErrors = true ; moreMessages.add( new Message("Last Name Required." )) ; }
        if ( command.address().equals("") )     { hasErrors = true ; moreMessages.add( new Message("Address Required." ) ); }
        if ( command.city().equals("") )        { hasErrors = true ; moreMessages.add( new Message("City Required." )) ; }
        if ( command.state().equals("") )       { hasErrors = true ; moreMessages.add( new Message("State Required." )) ; }
        if ( command.zip().equals("") )         { hasErrors = true ; moreMessages.add( new Message("Zip Required." ) ); }
        if ( command.phone().equals("") )       { hasErrors = true ; moreMessages.add( new Message("Phone Required." )) ; }
        if ( command.cardnum().equals("") )     { hasErrors = true ; moreMessages.add( new Message("Credit Card Number Required." ) ); }
        if ( command.cardexpmon().equals("") )  { hasErrors = true ; moreMessages.add( new Message("Credit Card Expiration Month Required." ) ); }
        if ( command.cardexpyear().equals("") ) { hasErrors = true ; moreMessages.add( new Message("Credit Card Expiration Year Required." )) ; }
        if ( command.cardcvv().equals("") )     { hasErrors = true ; moreMessages.add( new Message("Credit Card CVV Required." )); }
        if ( command.email().equals("") )       { hasErrors = true ; moreMessages.add( new Message("Email Address Required." )); }

        // regex validations: https://www.vogella.com/tutorials/JavaRegularExpressions/article.html
        if ( !command.zip().matches("\\d{5}") )                             { hasErrors = true ; moreMessages.add( new Message("Invalid Zip Code: " + command.zip() )) ; }
        if ( !command.phone().matches("[(]\\d{3}[)] \\d{3}-\\d{4}") )       { hasErrors = true ; moreMessages.add( new Message("Invalid Phone Number: " + command.phone() )) ; }
        if ( !command.cardnum().matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}") )    { hasErrors = true ; moreMessages.add( new Message("Invalid Card Number: " + command.cardnum() )) ; }
        if ( !command.cardexpyear().matches("\\d{4}") )                     { hasErrors = true ; moreMessages.add( new Message("Invalid Card Expiration Year: " + command.cardexpyear() )) ; }
        if ( !command.cardcvv().matches("\\d{3}") )                         { hasErrors = true ; moreMessages.add( new Message("Invalid Card CVV: " + command.cardcvv() )) ; }

        // validate months of the year
        if ( months.get( command.cardexpmon()) == null )  { hasErrors = true ; moreMessages.add(new Message( "Invalid Card Expiration Month: " + command.cardexpmon() )) ; }

        // validate states of 50 U.S. states
        if ( states.get( command.state()) == null )  { hasErrors = true ; moreMessages.add(new Message( "Invalid State: " + command.state() )) ; }

        if (hasErrors) {
            String content = "";
            for ( Message m : moreMessages ) {
                System.out.println( m.getMessage() ) ;
                content = content +  m.getMessage() + ",\n";
            }
            model.addAttribute( "message", content) ;
            return "creditcards";
        }

        /* create order number */
        Random rand = new Random();
        int min = 1000000;
        int max = 9999999;
        int order = rand.nextInt((max - min) + 1) + min;
        String orderNumber = String.valueOf(order) ;

        /*Auth Request*/
        AuthRequest auth = new AuthRequest() ;
        auth.reference = orderNumber ;
        auth.billToFirstName = command.firstname() ;
        auth.billToLastName = command.lastname() ;
        auth.billToAddress = command.address() ;
        auth.billToCity = command.city() ;
        auth.billToState = command.state() ;
        auth.billToZipCode = command.zip() ;
        auth.billToPhone = command.phone() ;
        auth.billToEmail =  command.email() ;
        auth.transactionAmount = "30.00" ;
        auth.transactionCurrency = "USD" ;
        auth.cardNumnber = command.cardnum() ;
        auth.cardExpMonth = months.get(command.cardexpmon()) ;
        auth.cardExpYear =  command.cardexpyear() ;
        auth.cardCVV = command.cardcvv() ;
        auth.cardType = CyberSourceAPI.getCardType( auth.cardNumnber ) ;

        if ( auth.cardType == null) {
            System.out.println( "Unsupported Credit Card Type." ) ;
            model.addAttribute( "message", "Unsupported Credit Card Type." ) ;
            return "creditcards";
        }

        boolean authValid = false;
        AuthResponse authResponse = new AuthResponse() ;
        System.out.println("\n\nAuth Request: " + auth.toJson() ) ;
        authResponse = api.authorize(auth) ;
        System.out.println("\n\nAuth Response: " + authResponse.toJson() ) ;
        if (authResponse.status.equals("AUTHORIZED") ) {
            authValid = true ;
        }
        else
        {
            authValid = false ;
            System.out.println( authResponse.message ) ;
            model.addAttribute( "message", authResponse.message ) ;
            return "creditcards";
        }

        boolean captureValid = false;
        CaptureRequest capture = new CaptureRequest() ;
        CaptureResponse captureResponse = new CaptureResponse() ;
        if ( authValid ) {
            capture.reference = orderNumber ;
            capture.paymentId = authResponse.id ;
            capture.transactionAmount = "30.00" ;
            capture.transactionCurrency = "USD" ;
            System.out.println("\n\nCapture Request: " + capture.toJson() ) ;
            captureResponse = api.capture(capture) ;
            System.out.println("\n\nCapture Response: " + captureResponse.toJson() ) ;
            if (captureResponse.status.equals("PENDING") ) {
                captureValid = true;
            }
            else
            {
                captureValid = false;
                System.out.println( captureResponse.message ) ;
                model.addAttribute( "message", captureResponse.message ) ;
                return "creditcards";
            }
        }


        if ( authValid && captureValid ) {

            command.setOrderNumber( orderNumber ) ;
            command.setTransactionAmount( "30.00") ;
            command.setTransactionCurrency( "USD") ;
            command.setAuthId( authResponse.id ) ;
            command.setAuthStatus( authResponse.status ) ;
            command.setCaptureId( captureResponse.id ) ;
            command.setCaptureStatus( captureResponse.status ) ;
            repository.save( command ) ;
            System.out.println( "Payment Sucessful! Order Number: " + orderNumber ) ;
            model.addAttribute( "message", "Payment Sucessful! Order Number: " + orderNumber ) ;
        }

        return "creditcards";
    }

}