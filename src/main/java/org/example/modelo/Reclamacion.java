package org.example.modelo;

                public class Reclamacion {
                    private String id;
                    private String cedulaCliente;
                    private String mensaje;
                    private String fecha;
                    private String estado;
                    private String respuesta; // New attribute

                    public Reclamacion(String id, String cedulaCliente, String mensaje, String fecha, String estado, String respuesta) {
                        this.id = id;
                        this.cedulaCliente = cedulaCliente;
                        this.mensaje = mensaje;
                        this.fecha = fecha;
                        this.estado = estado;
                        this.respuesta = respuesta;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getCedulaCliente() {
                        return cedulaCliente;
                    }

                    public void setCedulaCliente(String cedulaCliente) {
                        this.cedulaCliente = cedulaCliente;
                    }

                    public String getMensaje() {
                        return mensaje;
                    }

                    public void setMensaje(String mensaje) {
                        this.mensaje = mensaje;
                    }

                    public String getFecha() {
                        return fecha;
                    }

                    public void setFecha(String fecha) {
                        this.fecha = fecha;
                    }

                    public String getEstado() {
                        return estado;
                    }

                    public void setEstado(String estado) {
                        this.estado = estado;
                    }

                    public String getRespuesta() {
                        return respuesta;
                    }

                    public void setRespuesta(String respuesta) {
                        this.respuesta = respuesta;
                    }

                    @Override
                    public String toString() {
                        return "Reclamacion{" +
                                "id='" + id + '\'' +
                                ", cedulaCliente='" + cedulaCliente + '\'' +
                                ", mensaje='" + mensaje + '\'' +
                                ", fecha='" + fecha + '\'' +
                                ", estado='" + estado + '\'' +
                                ", respuesta='" + respuesta + '\'' +
                                '}';
                    }
                }