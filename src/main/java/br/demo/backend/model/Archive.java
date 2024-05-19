package br.demo.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_archive")
public class Archive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

    public Archive(MultipartFile file) {
        this.name = file.getOriginalFilename();
        this.type = file.getContentType();
        try {
            this.data = file.getBytes();
        } catch (IOException ignore) {
        }
    }
}
