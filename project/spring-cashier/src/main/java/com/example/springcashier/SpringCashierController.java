package com.example.springcashier;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Random;
import com.example.springcashier.model.Order;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import java.util.List;

//import com.example.starbucks-api;

@Slf4j
@Controller
@RequestMapping("/user/starbucks")
public class SpringCashierController {

    @Autowired
    RestTemplate eRepo;

    //private static final String api_endpoint = "http://{API_HOST:34.68.215.55:80/api}";
    private static final String api_host = System.getenv("API_HOST") != null ? System.getenv("API_HOST") : "34.68.215.55";
    private static final String api_endpoint = "http://" + api_host + ":80/api";
    private static final String api_key = System.getenv("API_KEY") != null ? System.getenv("API_KEY") : "Zkfokey2311";
//    private static final String secret_key = "kwRg54x2Go9iEdl49jFENRM12Mp711QI";
//    private static final String register_id = "5012349";

    @GetMapping
    public String getAction( @ModelAttribute("command") Command command, 
                            Model model, HttpSession session) {

        String message = "" ;

        command.setRegister( "5012349" ) ;
        message = "Starbucks Reserved Order" + "\n\n" +
            "Register: " + command.getRegister() + "\n" +
            "Status:   " + "Ready for New Order"+ "\n" ;

        String server_ip = "" ;
        String host_name = "" ;

        try { 
            InetAddress ip = InetAddress.getLocalHost() ;
            server_ip = ip.getHostAddress() ;
            host_name = ip.getHostName() ;
        } catch (Exception e) { }

        model.addAttribute( "message", message ) ;
        model.addAttribute( "server",  host_name + "/" + server_ip ) ;

        return "starbucks" ;

    }

    @PostMapping
    public String postAction(@Valid @ModelAttribute("command") Command command,  
                            @RequestParam(value="action", required=true) String action,
                            Errors errors, Model model, HttpServletRequest request) {

        String message = "" ;

        log.info( "Action: " + action ) ;
        command.setRegister( command.getStores() ) ;
        log.info( "Command: " + command ) ;


        //get order endpoint
        String getEndpoint = api_endpoint + "/order/register/" + command.getRegister()+ "?apikey=" + api_key;

        /* Process Post Action */
        if ( action.equals("Place Order") ) {

        try
        {
            Order active = eRepo.getForObject(getEndpoint, Order.class, command.getRegister());
            message = "Starbucks Reserved Order" + "\n\n"
                    +"You already have an active order, please clear to place an order.";
        }
        catch(Exception e) {
            try
            {
                Order order = new Order();
                order.setDrink(command.getDrink());
                order.setMilk(command.getMilk());
                order.setSize(command.getSize());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Order> requestEntity = new HttpEntity<>(order, headers);
                ResponseEntity<Order> responseEntity = eRepo.postForEntity(getEndpoint, requestEntity, Order.class);
                Order responseOrder = responseEntity.getBody();

                message = "Starbucks Reserved Order" + "\n\n" +
                        "Drink: " + responseOrder.getDrink() + "\n" +
                        "Milk:  " + responseOrder.getMilk() + "\n" +
                        "Size:  " + responseOrder.getSize() + "\n" +
                        "Total: " + responseOrder.getTotal() + "\n" +
                        "\n" +
                        "Register: " + responseOrder.getRegister() + "\n" +
                        "Status:   " + responseOrder.getStatus() + "\n";
            }
            catch(Exception x)
            {
                message = "Starbucks Reserved Order" + "\n\n" + "The size you have inputted is incorrect in correspondence with the order, please try again." + "\n\n";
            }
        }

        }
        else if ( action.equals("Get Order") ) {

            getEndpoint = api_endpoint + "/order/register/{regid}" + "?apikey=" + api_key;
            try
            {
                Order order = eRepo.getForObject(getEndpoint, Order.class, command.getRegister());
                message = "Starbucks Reserved Order" + "\n\n" +
                        "Drink: " + order.getDrink() + "\n" +
                        "Milk:  " + order.getMilk() + "\n" +
                        "Size:  " + order.getSize() + "\n" +
                        "Total: " + order.getTotal() + "\n" +
                        "\n" +
                        "Register: " + order.getRegister() + "\n" +
                        "Status:   " + order.getStatus() + "\n";
            }
            catch(Exception e)
            {
                message = "Starbucks Reserved Order" + "\n\n" +
                        "Register: " + command.getRegister() + "\n" +
                        "Status:   " + "Ready for New Order"+ "\n";
            }
        } 
        else if ( action.equals("Clear Order") ) {

            try
            {
                eRepo.delete(getEndpoint, command.getRegister());
                message = "Starbucks Reserved Order" + "\n\n" +
                        "Register: " + command.getRegister() + "\n" +
                        "Status:   " + "Ready for New Order"+ "\n" ;
            }
            catch(Exception e)
            {
                message = "Starbucks Reserved Order" + "\n\n" +
                        "Register: " + command.getRegister() + "\n" +
                        "Status:   " + "Ready for New Order"+ "\n" ;
            }

        }

        command.setMessage( message ) ;

        String server_ip = "" ;
        String host_name = "" ;
        try { 
            InetAddress ip = InetAddress.getLocalHost() ;
            server_ip = ip.getHostAddress() ;
            host_name = ip.getHostName() ;
        } catch (Exception e) { }

        model.addAttribute( "message", message ) ;
        model.addAttribute( "server",  host_name + "/" + server_ip ) ;

        return "starbucks" ;

    }

//    public String getDrink()
//    {
//        String[] DRINK_OPTIONS = { "Caffe Latte", "Caffe Americano", "Caffe Mocha", "Espresso", "Cappuccino" };
//        Random rand = new Random();
//
//        int randomNumber = rand.nextInt(5);
//        return DRINK_OPTIONS[randomNumber];
//    }
//
//    public String getMilk()
//    {
//        String[] MILK_OPTIONS  = { "Whole Milk", "2% Milk", "Nonfat Milk", "Almond Milk", "Soy Milk" };
//        Random rand = new Random();
//
//        int randomNumber = rand.nextInt(5);
//        return MILK_OPTIONS[randomNumber];
//    }
//
//    public String getSize()
//    {
//        String[] SIZE_OPTIONS  = {"Short","Tall", "Grande", "Venti", "Your Own Cup"};
//        Random rand = new Random();
//
//        int randomNumber = rand.nextInt(4);
//        return SIZE_OPTIONS[randomNumber];
//    }

}

