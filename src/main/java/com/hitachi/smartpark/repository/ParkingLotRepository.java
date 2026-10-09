package com.hitachi.smartpark.repository;

import com.hitachi.smartpark.entity.ParkingLot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {
    ParkingLot findByLotId(String lotId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ParkingLot p where p.lotId = :lotId")
    ParkingLot findByLotIdForUpdate(String lotId);
}
