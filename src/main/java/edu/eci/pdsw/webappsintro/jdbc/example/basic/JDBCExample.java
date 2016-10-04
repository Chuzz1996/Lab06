/*
 * Copyright (C) 2015 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.pdsw.webappsintro.jdbc.example.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class JDBCExample {
    
    public static void main(String args[]){
        try {
            String url="jdbc:mysql://desarrollo.is.escuelaing.edu.co:3306/bdprueba";
            String driver="com.mysql.jdbc.Driver";
            String user="bdprueba";
            String pwd="bdprueba";
                        
            Class.forName(driver);
            Connection con=DriverManager.getConnection(url,user,pwd);
            con.setAutoCommit(false);
                 
            //PreparedStatement valorPedido;
            
            //valorPedido.addBatch("Valor total pedido 1:"+valorTotalPedido(con, 1));
            //valorPedido.
            
            System.out.println("Valor total pedido 1:"+valorTotalPedido(con, 1));
            
            List<String> prodsPedido=nombresProductosPedido(con, 1);
            
            
            System.out.println("Productos del pedido 1:");
            System.out.println("-----------------------");
            for (String nomprod:prodsPedido){
                System.out.println(nomprod);
            }
            System.out.println("-----------------------");
            
            
            int suCodigoECI=20134423;
            registrarNuevoProducto(con, suCodigoECI, "SU NOMBRE", 99999999);            
            con.commit();
            
            cambiarNombreProducto(con, suCodigoECI, "EL NUEVO NOMBRE");
            con.commit();
            
            
            con.close();
                                   
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(JDBCExample.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    /**
     * Agregar un nuevo producto con los parámetros dados
     * @param con la conexión JDBC
     * @param codigo
     * @param nombre
     * @param precio
     * @throws SQLException 
     */
    public static void registrarNuevoProducto(Connection con, int codigo, String nombre,int precio) throws SQLException{
        //Crear preparedStatement
        //Asignar parámetros
        //usar 'execute'
        /*
        PreparedStatement RegistoDenuevoProducto = con.prepareStatement("insert into ORD_PRODUCTOS(CODIGO,NOMBRE,PRECIO) values(?,?,?)");
        RegistoDenuevoProducto.setInt(1, codigo);
        RegistoDenuevoProducto.setString(2, nombre);
        RegistoDenuevoProducto.setInt(3, precio);
        RegistoDenuevoProducto.executeUpdate();*/
        con.commit();
        
    }
    
    /**
     * Consultar los nombres de los productos asociados a un pedido
     * @param con la conexión JDBC
     * @param codigoPedido el código del pedido
     * @return 
     */
    public static List<String> nombresProductosPedido(Connection con, int codigoPedido){
        List<String> np=new LinkedList<>();
        
        //Crear prepared statement
        //asignar parámetros
        //usar executeQuery
        //Sacar resultados del ResultSet
        //Llenar la lista y retornarla
        try{
            PreparedStatement ProductosPedidos = con.prepareStatement("SELECT NOMBRE FROM ORD_PRODUCTOS WHERE codigo = ?");
            ProductosPedidos.setInt(1, codigoPedido);
            ResultSet rs = ProductosPedidos.executeQuery();
            while(rs.next()){
                np.add(rs.getString(1));
            }
        }catch(SQLException ex){
            System.out.println("FALLO EN NOMBRE PRODUCTOS");
        }
        
        return np;
    }

    
    /**
     * Calcular el costo total de un pedido
     * @param con
     * @param codigoPedido código del pedido cuyo total se calculará
     * @return el costo total del pedido (suma de: cantidades*precios)
     */
    public static int valorTotalPedido(Connection con, int codigoPedido){
        
        //Crear prepared statement
        //asignar parámetros
        //usar executeQuery
        //Sacar resultado del ResultSet
        int res = 0;
        try{
            String consulta = "select (SUM(ORP.precio)*SUM(ODP.cantidad)) from ORD_PEDIDOS OP join ORD_DETALLES_PEDIDO ODP(OP.Codigo=ODP.pedidos)"
                    + " join ORD_PRODUCTOS ORP(ORP.Codigo=ODP.producto) where OP.codigo = ?";
            PreparedStatement valorTotal = con.prepareStatement(consulta);
            valorTotal.setInt(1, codigoPedido);
            res = valorTotal.executeUpdate();
        }catch(SQLException ex){
            System.out.println("FALLO EN VALOR PEDIDO");
        }
        return res;
    }
    

    /**
     * Cambiar el nombre de un producto
     * @param con
     * @param codigoProducto codigo del producto cuyo nombre se cambiará
     * @param nuevoNombre el nuevo nombre a ser asignado
     */
    public static void cambiarNombreProducto(Connection con, int codigoProducto, 
            String nuevoNombre){
        
        //Crear prepared statement
        //asignar parámetros
        //usar executeUpdate
        //verificar que se haya actualizado exactamente un registro
        String update = "UPDATE ORD_PRODUCTOS SET NOMBRE = ? WHERE CODIGO = ?";
        try{
            PreparedStatement Nombre = con.prepareStatement(update);
            Nombre.setInt(1, codigoProducto);
            Nombre.setString(2, nuevoNombre);
            Nombre.executeUpdate();
       }catch(SQLException ex){
            System.out.println("FALLO EN CAMBIAR NOMBRE DEL PRODUCTO");
        }
    }
    
    
    
}
