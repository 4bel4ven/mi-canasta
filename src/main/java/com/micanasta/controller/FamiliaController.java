package com.micanasta.controller;

import com.micanasta.dto.CrearFamiliaDTO;
import com.micanasta.dto.FamiliaBusquedaMiembrosDto;
import com.micanasta.exception.ExistingFamilyFoundException;
import com.micanasta.exception.FamilyNotFoundException;
import com.micanasta.service.FamiliaService;
import com.micanasta.model.Familia;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FamiliaController {

    @Autowired
    private FamiliaService familiaService;

    @Autowired
    private UsuarioPorFamiliaService usuarioPorFamiliaService;

    @PostMapping("/familias")
    public ResponseEntity<?> crearFamilia(@Valid @RequestBody CrearFamiliaDTO familiaDto) {
        try {
             familiaService.crearGrupoFamiliar(familiaDto);
        } catch (ExistingFamilyFoundException existingFamilyFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(existingFamilyFoundException.exceptionDto);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Se ha creado el grupo familiar");
    }

    @GetMapping("/familias/{nombreFamilia}/usuarios")
    public ResponseEntity<?> buscarMiembrosGrupoFamiliarPorNombreFamilia(@PathVariable("nombreFamilia") String nombreFamilia) throws FamilyNotFoundException {

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(familiaService.buscarMiembrosGrupoFamiliarPorNombreFamilia(nombreFamilia));
        } catch (FamilyNotFoundException familyNotFoundException) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(familyNotFoundException.exceptionDto);
        }
    }

    @DeleteMapping("/familias/{nombreFamilia}/usuarios/{dni}")
    public ResponseEntity<?> deleteUsuarioDeFamilia(String adminDni, @PathVariable String dni ) throws UserToDeleteIsAdminException, UserNotAdminException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioPorFamiliaService.Remove(adminDni, dni));
        }
        catch(UserNotAdminException userNotAdminException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userNotAdminException.exceptionDto);
        }
        catch(UserToDeleteIsAdminException userToDeleteIsAdminException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userToDeleteIsAdminException.exceptionDto);
        }
    }

    @PutMapping("/familias/{nombreFamilia}")
    public ResponseEntity<?> desactivarSolicitudes(@PathVariable("nombreFamilia")  String nombreFamilia, String dni){
        try{
            familiaService.desactivarSolicitudes(nombreFamilia, dni);
        } catch (FamilyNotFoundException familyNotFoundException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(familyNotFoundException.exceptionDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Se desactivó realizar solicitudes y se eliminaron las existentes");
    }

}
