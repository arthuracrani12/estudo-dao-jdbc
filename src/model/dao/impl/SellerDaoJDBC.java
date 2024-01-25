package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

	//Dao tem uma dependencia com a conexao, teremoos o objeto conn disponivel para implementar as opera��es
	private Connection conn;
	//formar a inje��o de depend�ncia 
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
			st.setInt(1, id);
			//executar comando st e salvar em rs
			rs = st.executeQuery();
			//para testar se tem algum resulado (se tem vendedo com o id) se n�o retorna null
			//implementa��o do metodo para retornar o vendedor pelo ID
			
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);
				return obj;
			}
			
					
			
			/* C�digo geral (instancia departamento e vendedor
			 * no c�digo geral transformamos em fun��o para reutilizarmos, c�digo a cima)
			 * separamos em Department instantiateDepartment(ResultSet rs) e
			 * Seller instantiateSeller 
			 * if (rs.next()) {
				Department dep = new Department();
				dep.setId(rs.getInt("DepartmentId"));
				dep.setName(rs.getString("DepName"));
				Seller obj = new Seller();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setEmail(rs.getString("Email"));
				obj.setBaseSalary(rs.getDouble("BaseSalary"));
				obj.setBirthDate(rs.getDate("BirthDate"));
				obj.setDepartment(dep);
				return obj;
			} */
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			//n�o fechamos a conex�o pois pode ser usado para fazer outra opera��o
		}
	}
		

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			
			//usamos List pois pode ter mais de um
			List<Seller> list = new ArrayList<>();
			//Usamos map para n�o repetir departamento (n�o podemos criar 1 departamento para cada vender se o departamento for o mesmo
			//por exemplo, se o mesmo departamento tiver 5 vendedores, cria apenas 1 departamento com o mesmo id)
			Map<Integer, Department> map = new HashMap<>();
			
			//como s�o v�rios v�rios usamos while e n�o IF
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				//se o departamento n�o existe crio ele e salvo no map para n�o criar novamente
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
