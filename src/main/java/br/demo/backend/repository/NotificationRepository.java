package br.demo.backend.repository;

import br.demo.backend.model.Notification;
import br.demo.backend.model.User;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long > {

    Collection<Notification> findAllByUser(User user);
}
