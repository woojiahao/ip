package cyrus.parser;

import cyrus.commands.CommandType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {
  private static final Parser PARSER = new Parser();

  @Test
  public void testParseWithEmptyLine() {
    assertEquals(ParseInfo.EMPTY, PARSER.parse(""));
    assertEquals(ParseInfo.EMPTY, PARSER.parse("  "));
  }

  @Test
  public void testParseWithUnknownCommand() {
    assertEquals(CommandType.UNKNOWN, PARSER.parse("unknown hi this is unknown").commandType);
  }

  @Test
  public void testParseWithNoArgument() {
    assertEquals(
        "",
        PARSER.parse("todo /option1 this /other other option!").getArgument()
    );
  }

  @Test
  public void testParseWithOptions() {
    HashMap<String, String> options =
        PARSER.parse("todo /a this is a /b b /c hello! / this should be together").getOptions();
    HashMap<String, String> expected = new HashMap<>();
    expected.put("a", "this is a");
    expected.put("b", "b");
    expected.put("c", "hello! / this should be together");
    for (var entry : expected.entrySet()) {
      assertTrue(options.containsKey(entry.getKey()));
      assertEquals(entry.getValue(), options.getOrDefault(entry.getKey(), "invalid"));
    }
  }

  @Test
  public void testParseWithoutOptions() {
    ParseInfo info = PARSER.parse("todo this is a todo");
    assertEquals(CommandType.ADD_TODO, info.commandType);
    assertEquals("this is a todo", info.getArgument());
    assertEquals(0, info.getOptions().size());
  }
}
