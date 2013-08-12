package spring.reference.service.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import spring.reference.model.Address;
import spring.reference.util.Logged;

@Logged
public interface AddressRepository extends JpaRepository<Address, Long> {
    Page<Address> findByIdAndActiveFlag(long id, Pageable pageable, boolean active);

    Page<Address> findByActiveFlag(Pageable pageable, boolean active);

    Page<Address> findByIdIn(Collection<Long> Ids, Pageable pageable);

    Page<Address> findByIdInAndActiveFlag(Collection<Long> Ids, boolean active, Pageable pageable);
}
