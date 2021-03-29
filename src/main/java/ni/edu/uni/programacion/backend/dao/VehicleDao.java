package ni.edu.uni.programacion.backend.dao;

import java.io.IOException;
import java.util.Collection;
import ni.edu.uni.programacion.backend.pojo.Vehicle;

/**
 *
 * @author yasser.membreno
 */
public interface VehicleDao extends Dao<Vehicle>
{
    Vehicle findById(int id) throws IOException;
    Collection<Vehicle> findByStatus(String status) throws IOException;
}
