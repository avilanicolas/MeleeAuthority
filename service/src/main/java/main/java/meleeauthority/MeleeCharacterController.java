package main.java.meleeauthority;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeleeCharacterController {

    public static MeleeDB meleeDB = null;

    private static final String ALL = "ALL";

    @RequestMapping("/character")
    public List<MeleeCharacter> character(@RequestParam(value="character", defaultValue=ALL) String name) {
        return getDB().getAllCharacters();
    }

    private MeleeDB getDB() {
        if (meleeDB == null) {
            meleeDB = Application.getDB();
        }

        return meleeDB;
    }
}
