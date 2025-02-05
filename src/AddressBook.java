import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AddressBook {
    private final HashMap<String, String> contacts;
    private final String fileName = "contactos.txt";

    public AddressBook(){
        contacts = new HashMap<>();
        load();
    }

    public void load() {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName)) ) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if(data.length == 2){
                    contacts.put(data[0].trim(), data[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar la lista de contactos. ");
        }
    }

    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, String> entry : contacts.entrySet()){
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("No se pudo guardar la información.");
        }
    }


    public static void main(String[] args) throws IOException {
        AddressBook addressBook = new AddressBook();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean running = true;

        do {
            System.out.println("---------- Menú Principal ----------");
            System.out.println();
            System.out.println("Selecciona una opcion del siguiente menú:");
            System.out.println("1. Crear nuevo contacto");
            System.out.println("2. Mostrar todos los contactos");
            System.out.println("3. Buscar contacto");
            System.out.println("4. Actualizar contacto");
            System.out.println("5. Borrar contacto");
            System.out.println("9. Terminar programa");


            switch ( reader.readLine() ){
                case "1" -> addressBook.crearContacto();
                case "2" -> addressBook.listContactos();
                case "3" -> addressBook.buscarContacto();
                case "4" -> addressBook.actualizarContacto();
                case "5" -> addressBook.deleteContacto();
                case "9" -> running = addressBook.salirPrograma();
                default -> System.out.println("Opción invalida.");

            }

        } while (running );
        //return;

    }

    private void buscarContacto() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-------- Buscar contacto -------");
        System.out.println("Ingresa el nombre del contacto que quieres buscar");
        try {
            String nombre = reader.readLine().trim();
            if (contacts.containsKey(nombre)){
                System.out.println("El telefono de " + nombre + " es " + contacts.get(nombre));
            } else {
                System.out.println("No hay ningún teléfono registrado para " + nombre);
            }

        } catch (IOException e) {
            System.err.println("Error al leer la entrada: " + e.getMessage());
        }
        System.out.println();
    }

    public void crearContacto() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-------- Agregar contacto -------");
        try {
            String nombre;
            String numero;

            do {
                System.out.println("Por favor, ingresa el nombre:");
                nombre = reader.readLine().trim();

                if (nombre.isEmpty()) {
                    System.out.println("El nombre no puede estar vacío. Inténtalo de nuevo.");
                }
            } while ( nombre.isEmpty());

            if ( contacts.containsKey(nombre)) {
                System.out.println("Este contacto ya existe en el directorio con el telefono " + contacts.get(nombre) );
                return;
            }



            do {
                System.out.println("Ingresa el numero (10 dígitos):");
                numero = reader.readLine().trim().replaceAll(" ","");

                if (!numero.matches("\\d{10}")) {
                    System.out.println("El numero sólo puede contener 10 dígitos. Inténtalo de nuevo.");
                }

                if (contacts.containsValue(numero)){
                    for (Map.Entry<String, String> entry : contacts.entrySet()){
                        if (entry.getValue().equals(numero) ) {
                           System.out.println("El número " + entry.getValue() +  " esta asignado actualmente a " + entry.getKey() );
                           return;
                        }

                    }

                }

            } while ( !numero.matches("\\d{10}"));


            contacts.put(nombre , numero);
            save();
            System.out.println(nombre + " ha sido agregado con el número " + numero);
            System.out.println();


        } catch (IOException e) {
            System.err.println("Error al leer la entrada: " + e.getMessage());
        }
    }

    public void listContactos(){
        System.out.println("--------- Agenda telefónica ---------");
        System.out.println();
        for (Map.Entry<String, String> entry : contacts.entrySet()){
            System.out.println("Nombre: " +  entry.getKey() + ", Teléfono: " + entry.getValue());
        }
        System.out.println();
    }

    public void actualizarContacto() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("-------- Agregar contacto -------");
        System.out.println("Ingresa el nombre del contacto que quieres actualizar");
        String nombre = reader.readLine().trim();
        String numero;

        if (!contacts.containsKey(nombre)) {
            System.out.println("No existe ningún registro con el nombre " + nombre);
        } else {
            System.out.println(nombre + " tiene el teléfono " + contacts.get(nombre));
            System.out.println("Ingresa el nuevo teléfono para " + nombre + " (10 dígitos):");
            do {
                numero = reader.readLine().trim().replaceAll(" ","");

                if (!numero.matches("\\d{10}")) {
                    System.out.println("El numero sólo puede contener 10 dígitos. Inténtalo de nuevo.");
                }

                contacts.replace(nombre,numero);
                save();
                System.out.println("El teléfono de " + nombre + " ha sido actualizado correctamente.");
            } while ( !numero.matches("\\d{10}"));

        }

        System.out.println();
    }

    public void deleteContacto(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            listContactos();
            System.out.println("¿Cuál contacto deseas eliminar?");
            String contactoAEliminar = reader.readLine().trim();
            if ( contacts.remove(contactoAEliminar) == null ) {
                System.out.println("No existe ningun usuario con ese nombre.");
            } else {
                System.out.println(contactoAEliminar +  " ha sido eliminado correctamente.");
                save();
            }
        } catch (IOException e) {
            System.err.println("Error al leer la entrada: " + e.getMessage());
        }

    }

    public boolean salirPrograma() {
        System.out.println("Saliendo del programa. ¡Hasta pronto!");
        return false;
    }
}
