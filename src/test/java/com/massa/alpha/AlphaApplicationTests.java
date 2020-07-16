package com.massa.alpha;

import com.massa.alpha.util.ShExcuteUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlphaApplicationTests {

    ShExcuteUtil shex = new ShExcuteUtil();

    @Test
    public void shExcutedTest() throws IOException, InterruptedException {

        String cmds = "sh /Users/atingle/app/test.sh";
        String[] callCmd = {"/bin/bash", "-c", cmds};
        Map map = shex.execCommend(callCmd);

        System.out.println(map);
    }

}
