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

    /**
     * Returns a {@link Page} containing every {@link Address} with one of the given <code>Ids</code>.
     * 
     * @param Ids
     * @param pageable
     * @return
     */
    Page<Address> findByIdIn(Collection<Long> Ids, Pageable pageable);

    /**
     * Returns a {@link Page} containing every {@link Address} with one of the given <code>Ids</code>, if the it's ActiveFlag matches the given value.
     * 
     * @param Ids
     * @param active
     * @param pageable
     * @return
     */
    Page<Address> findByIdInAndActiveFlag(Collection<Long> Ids, boolean active, Pageable pageable);
}
