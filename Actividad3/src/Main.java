import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;


class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/escuela";
    private static final String USER = "root";
    private static final String PASSWORD = "andersonrey123";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}


class Persona {
    protected String nit;
    protected String nombres;
    protected String apellidos;
    protected String direccion;
    protected String telefono;
    protected Date fecha_de_nacimiento;

 
    public Persona(String nit, String nombres, String apellidos, String direccion, String telefono, Date fechaNacimiento) {
        this.nit = nit;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        this.fecha_de_nacimiento = fechaNacimiento;
    }

   
    public int crear() {
        int generatedId = -1; 
        try (Connection conn = Conexion.conectar()) {
            String query = "INSERT INTO persona (nit, nombres, apellidos, direccion, telefono, fecha_de_nacimiento) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nit);
            stmt.setString(2, nombres);
            stmt.setString(3, apellidos);
            stmt.setString(4, direccion);
            stmt.setString(5, telefono);
            stmt.setDate(6, fecha_de_nacimiento);
            stmt.executeUpdate();

       
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }
            System.out.println("Persona creada exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId; 
    }

    public void leer() {
        try (Connection conn = Conexion.conectar()) {
            String query = "SELECT * FROM persona WHERE nit = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nit);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Nit: " + rs.getString("nit"));
                System.out.println("Nombres: " + rs.getString("nombres"));
                System.out.println("Apellidos: " + rs.getString("apellidos"));
                System.out.println("Dirección: " + rs.getString("direccion"));
                System.out.println("Teléfono: " + rs.getString("telefono"));
                System.out.println("Fecha Nacimiento: " + rs.getDate("fecha_de_nacimiento"));
            } else {
                System.out.println("Persona no encontrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar() {
        try (Connection conn = Conexion.conectar()) {
            String query = "UPDATE persona SET nombres = ?, apellidos = ?, direccion = ?, telefono = ?, fecha_de_nacimiento = ? WHERE nit = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nombres);
            stmt.setString(2, apellidos);
            stmt.setString(3, direccion);
            stmt.setString(4, telefono);
            stmt.setDate(5, fecha_de_nacimiento);
            stmt.setString(6, nit);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Persona actualizada exitosamente.");
            } else {
                System.out.println("No se encontró a la persona para actualizar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void borrar() {
        try (Connection conn = Conexion.conectar()) {
            String query = "DELETE FROM persona WHERE nit = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nit);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Persona eliminada exitosamente.");
            } else {
                System.out.println("No se encontró a la persona para eliminar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters y Setters
    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFechaNacimiento() {
        return fecha_de_nacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fecha_de_nacimiento = fechaNacimiento;
    }
}


class Docente extends Persona {
    private String codigoDocente;
    private double salario;
    private Date fechaIngresoLaborar;
    private Date fechaIngresoRegistro;

   
    public Docente(String nit, String nombres, String apellidos, String direccion, String telefono, Date fechaNacimiento,
                   String codigoDocente, double salario, Date fechaIngresoLaborar, Date fechaIngresoRegistro) {
        super(nit, nombres, apellidos, direccion, telefono, fechaNacimiento);
        this.codigoDocente = codigoDocente;
        this.salario = salario;
        this.fechaIngresoLaborar = fechaIngresoLaborar;
        this.fechaIngresoRegistro = fechaIngresoRegistro;
    }

    
    public void crearDocente() {
        int personaId = super.crear(); 
        if (personaId != -1) { 
            try (Connection conn = Conexion.conectar()) {
                String query = "INSERT INTO docente (codigo_docente, salario, fecha_ingreso_laboral, fecha_ingreso_registro, persona_id) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, codigoDocente);
                stmt.setDouble(2, salario);
                stmt.setDate(3, fechaIngresoLaborar);
                stmt.setDate(4, fechaIngresoRegistro);
                stmt.setInt(5, personaId); // Use the generated persona ID
                stmt.executeUpdate();
                System.out.println("Docente creado exitosamente.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void leer() {
        super.leer(); 
        try (Connection conn = Conexion.conectar()) {
            String query = "SELECT * FROM docente WHERE codigo_docente = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, codigoDocente);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Código Docente: " + rs.getString("codigo_docente"));
                System.out.println("Salario: " + rs.getDouble("salario"));
                System.out.println("Fecha Ingreso Laboral: " + rs.getDate("fecha_ingreso_laboral"));
                System.out.println("Fecha Ingreso Registro: " + rs.getDate("fecha_ingreso_registro"));
            } else {
                System.out.println("Docente no encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar() {
        super.actualizar();  // Llama a actualizar() de Persona
        try (Connection conn = Conexion.conectar()) {
            String query = "UPDATE docente SET salario = ?, fecha_ingreso_laboral = ?, fecha_ingreso_registro = ? WHERE codigo_docente = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, salario);
            stmt.setDate(2, fechaIngresoLaborar);
            stmt.setDate(3, fechaIngresoRegistro);
            stmt.setString(4, codigoDocente);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Docente actualizado exitosamente.");
            } else {
                System.out.println("No se encontró al docente para actualizar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void borrar() {
      
        try (Connection conn = Conexion.conectar()) {
            String query = "DELETE FROM docente WHERE codigo_docente = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, codigoDocente);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                  super.borrar();  // Llama a borrar() de Persona
                System.out.println("Docente eliminado exitosamente.");
            } else {
                System.out.println("No se encontró al docente para eliminar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getCodigoDocente() {
        return codigoDocente;
    }

    public void setCodigoDocente(String codigoDocente) {
        this.codigoDocente = codigoDocente;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public Date getFechaIngresoLaborar() {
        return fechaIngresoLaborar;
    }

    public void setFechaIngresoLaborar(Date fechaIngresoLaborar) {
        this.fechaIngresoLaborar = fechaIngresoLaborar;
    }

    public Date getFechaIngresoRegistro() {
        return fechaIngresoRegistro;
    }

    public void setFechaIngresoRegistro(Date fechaIngresoRegistro) {
        this.fechaIngresoRegistro = fechaIngresoRegistro;
    }
}


public class Main {
    public static void main(String[] args) {
       
        Docente docente = new Docente("123456789", "Juan", "Pérez", "Calle Falsa 123", "555-1234",
                Date.valueOf("1980-01-01"), "D001", 1500.00, Date.valueOf("2020-05-01"), Date.valueOf("2020-05-10"));

       
        docente.crearDocente();    // Crear en la base de datos
        docente.leer();     // Leer de la base de datos
        docente.setSalario(1800.00); // Actualizar salario
        docente.actualizar();  // Actualizar en la base de datos
        docente.borrar();   // Borrar de la base de datos
    }
}