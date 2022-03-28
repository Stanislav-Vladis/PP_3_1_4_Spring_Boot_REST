package academy.kata.platform.spring.spring_boot_rest.service;

import academy.kata.platform.spring.spring_boot_rest.dao.RoleRepository;
import academy.kata.platform.spring.spring_boot_rest.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImp implements RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role findRoleById(long id) {
        return roleRepository.findRoleById(id);
    }

    @Override
    public Role findRoleByRole(String roleName) {
        return roleRepository.findRoleByRole(roleName);
    }

    @Override
    public void saveOrUpdate(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void removeRoleById(long id) {
        roleRepository.deleteById(id);
    }

}
