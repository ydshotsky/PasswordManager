package com.miniproject.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name ="users")
public class User {
    @Id
    @SequenceGenerator(name="user_seq",sequenceName = "user_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_seq")
    Long id;

    @Column(unique = true)
    private String username;

    //hashed master key
    @Column(unique = true)
    private String password;

    // salt for PBKDF2 / Argon2
    @Column(nullable = false,columnDefinition = "BYTEA",unique = true)
    private byte[] kdfSalt;

    // all passwords linked
    //cascadeType.all remove all passwords if related user deleted itself
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<VaultPassword> passwords;

}
