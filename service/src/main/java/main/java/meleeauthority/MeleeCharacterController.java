package main.java.meleeauthority;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.ImmutableList;

@RestController
public class MeleeCharacterController {

    private static MeleeDB meleeDB = null;
    private static List<String> characterNames = null;

    private static final String ALL = "ALL";

    @RequestMapping("/character")
    public List<MeleeCharacter> character(@RequestParam(value="name", defaultValue=ALL) String name) {
        List<MeleeCharacter> list;
        if (name.equals(ALL)) {
            list = getDB().getAllCharacters();
        } else {
            if (validName(name)) {
                list = ImmutableList.of(getDB().getCharacter(name));
            } else {
                list = ImmutableList.of();
            }
        }

        return list;
    }

    private boolean validName(String name) {
        return getCharacterNames().contains(name);        
    }

    private List<String> getCharacterNames() {
        if (characterNames == null) {
            characterNames = getDB().getCharacterNames();
        }

        return characterNames;
    }

    private MeleeDB getDB() {
        if (meleeDB == null) {
            meleeDB = Application.getDB();
        }

        return meleeDB;
    }
}
