import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

 class Customer {
    private String firstName;
    private String lastName;
    private String street;
    private String address;
    private String city;
    private String state;
    private String email;
    private String phone;

    // Constructor
    public Customer(String firstName, String lastName, String street, String address, String city, String state, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.address = address;
        this.city = city;
        this.state = state;
        this.email = email;
        this.phone = phone;
    }

    // Getters and setters (optional)
    // You can add getters and setters for each field if needed.

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", street='" + street + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}


class CustomerApi {
    public static void createCustomer(String bearerToken, String firstName, String lastName, String street, String address, String city, String state, String email, String phone) throws Exception {
        String createCustomerUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=create";
        URL url = new URL(createCustomerUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + bearerToken);

        conn.setDoOutput(true);

        String requestBody = "{\"first_name\":\"" + firstName + "\", \"last_name\":\"" + lastName + "\", \"street\":\"" + street + "\", \"address\":\"" + address + "\", \"city\":\"" + city + "\", \"state\":\"" + state + "\", \"email\":\"" + email + "\", \"phone\":\"" + phone + "\"}";
        conn.getOutputStream().write(requestBody.getBytes());

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            System.out.println("Customer created successfully.");
        } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
            // Handle first name or last name missing error
            throw new Exception("Error: First Name or Last Name is missing");
        } else {
            // Handle other errors
            throw new Exception("Error: Unable to create customer.");
        }
    }

   static List<Customer> getCustomerList(String bearerToken) throws Exception {
        String getCustomerListUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
        URL url = new URL(getCustomerListUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + bearerToken);

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response and convert it to a List<Customer> object
            Gson gson = new Gson();
            Type customerListType = new TypeToken<List<Customer>>() {}.getType();
            List<Customer> customers = gson.fromJson(response.toString(), customerListType);
            return customers;
        } else {
            // Handle errors
            throw new Exception("Error: Unable to get customer list.");
        }
    }
 static void deleteCustomer(String bearerToken, String customerUUID) throws Exception {
        String deleteCustomerUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=delete&uuid=" + customerUUID;
        URL url = new URL(deleteCustomerUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + bearerToken);

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Customer deleted successfully.");
        } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
            // Handle UUID not found error
            throw new Exception("Error: UUID not found");
        } else {
            // Handle other errors
            throw new Exception("Error: Unable to delete customer.");
        }
    }

     static void updateCustomer(String bearerToken, String customerUUID, String firstName, String lastName, String street, String address, String city, String state, String email, String phone) throws Exception {
        String updateCustomerUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=update&uuid=" + customerUUID;
        URL url = new URL(updateCustomerUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + bearerToken);

        conn.setDoOutput(true);

        String requestBody = "{\"first_name\":\"" + firstName + "\", \"last_name\":\"" + lastName + "\", \"street\":\"" + street + "\", \"address\":\"" + address + "\", \"city\":\"" + city + "\", \"state\":\"" + state + "\", \"email\":\"" + email + "\", \"phone\":\"" + phone + "\"}";
        conn.getOutputStream().write(requestBody.getBytes());

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Customer updated successfully.");
        } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
            // Handle UUID not found or Body is Empty error
            throw new Exception("Error: UUID not found or Body is Empty");
        } else {
            // Handle other errors
            throw new Exception("Error: Unable to update customer.");
        }
    }
}
 class MainApplication {
    public static void main(String[] args) {
        try {
            // Step 1: Get user credentials from user input (email and password)
            String loginId = "test@sunbasedata.com";
            String password = "Test@123";

            // Step 2: Authenticate user and get the bearer token
            String bearerToken = AuthenticationApi.authenticate(loginId, password);

            // Step 3: Display the customer list
            List<Customer> customers = CustomerApi.getCustomerList(bearerToken);
            displayCustomerList(customers);

            // Step 4: Create a new customer
            String newFirstName = "Jane";
            String newLastName = "Doe";
            String newStreet = "Elvnu Street";
            String newAddress = "H no 2";
            String newCity = "Delhi";
            String newState = "Delhi";
            String newEmail = "sam@gmail.com";
            String newPhone = "12345678";
            CustomerApi.createCustomer(bearerToken, newFirstName, newLastName, newStreet, newAddress, newCity, newState, newEmail, newPhone);

            // Step 5: Display the updated customer list after adding a new customer
            customers = CustomerApi.getCustomerList(bearerToken);
            displayCustomerList(customers);

            // Step 6: Update an existing customer (assuming you have the UUID of the customer)
            String customerUUIDToUpdate = "UUID_OF_CUSTOMER_TO_UPDATE";
            String updatedFirstName = "UpdatedFirstName";
            String updatedLastName = "UpdatedLastName";
            String updatedStreet = "UpdatedStreet";
            String updatedAddress = "UpdatedAddress";
            String updatedCity = "UpdatedCity";
            String updatedState = "UpdatedState";
            String updatedEmail = "updated_email@example.com";
            String updatedPhone = "9876543210";
            CustomerApi.updateCustomer(bearerToken, customerUUIDToUpdate, updatedFirstName, updatedLastName, updatedStreet, updatedAddress, updatedCity, updatedState, updatedEmail, updatedPhone);

            // Step 7: Display the updated customer list after updating a customer
            customers = CustomerApi.getCustomerList(bearerToken);
            displayCustomerList(customers);

            // Step 8: Delete a customer (assuming you have the UUID of the customer)
            String customerUUIDToDelete = "UUID_OF_CUSTOMER_TO_DELETE";
            CustomerApi.deleteCustomer(bearerToken, customerUUIDToDelete);

            // Step 9: Display the updated customer list after deleting a customer
            customers = CustomerApi.getCustomerList(bearerToken);
            displayCustomerList(customers);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to display the customer list
    private static void displayCustomerList(List<Customer> customers) {
        System.out.println("Customer List:");
        for (Customer customer : customers) {
            System.out.println(customer.toString());
        }
        System.out.println();
    }
}
