/**
 *
 */
package joshua.decoder.io;

import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 *
 */
public class DeNormalizeTest {

  private String tokenized;

  /**
   * @throws java.lang.Exception
   */
  @BeforeTest
  protected void setUp() throws Exception {
    tokenized = "my son 's friend , however , plays a high - risk game .";
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterTest
  protected void tearDown() throws Exception {
  }

  /**
   * Test method for {@link joshua.decoder.io.DeNormalize#processSingleLine(java.lang.String)}.
   */
  @Test(enabled = true)
  public void testProcessSingleLine() {
    String actual = DeNormalize.processSingleLine(tokenized);
    String expected = "My son 's friend, however, plays a high-risk game.";
    assertEquals(actual, expected);
  }

  /**
   * Test method for
   * {@link joshua.decoder.io.DeNormalize#testCapitalizeFirstLetter(java.lang.String)}.
   */
  @Test
  public void testCapitalizeFirstLetter() throws Exception {
    String actual = DeNormalize.capitalizeFirstLetter(tokenized);
    String expected = "My son 's friend , however , plays a high - risk game .";
    assertEquals(actual, expected);
  }

  /**
   * Test method for
   * {@link joshua.decoder.io.DeNormalize#testCapitalizeFirstLetter(java.lang.String)}.
   */
  @Test
  public void testCapitalizeFirstLetter_empty() throws Exception {
    String actual = DeNormalize.capitalizeFirstLetter("");
    String expected = "";
    assertEquals(actual, expected);
  }

  /**
   * Test method for
   * {@link joshua.decoder.io.DeNormalize#testCapitalizeFirstLetter(java.lang.String)}.
   */
  @Test
  public void testCapitalizeFirstLetter_singleNumberCharacter() throws Exception {
    String actual = DeNormalize.capitalizeFirstLetter("1");
    String expected = "1";
    assertEquals(actual, expected);
  }

  /**
   * Test method for
   * {@link joshua.decoder.io.DeNormalize#testCapitalizeFirstLetter(java.lang.String)}.
   */
  @Test
  public void testCapitalizeFirstLetter_singleLetterCharacter() throws Exception {
    String actual = DeNormalize.capitalizeFirstLetter("a");
    String expected = "A";
    assertEquals(actual, expected);
  }

  /**
   * Test method for {@link joshua.decoder.io.DeNormalize#testJoinPeriodsCommas(java.lang.String)}.
   */
  @Test
  public void testJoinPeriodsCommas() throws Exception {
    String actual = DeNormalize.joinPeriodsCommas(tokenized);
    String expected = "my son 's friend, however, plays a high - risk game.";
    assertEquals(actual, expected);
  }

  /**
   * Test method for {@link joshua.decoder.io.DeNormalize#testJoinPeriodsCommas(java.lang.String)}.
   */
  @Test
  public void testJoinPeriodsCommas_empty() throws Exception {
    String actual = DeNormalize.joinPeriodsCommas("");
    String expected = "";
    assertEquals(actual, expected);
  }

  /**
   * Test method for {@link joshua.decoder.io.DeNormalize#testJoinHyphen(java.lang.String)}.
   */
  @Test
  public void testJoinHyphen() throws Exception {
    String actual = DeNormalize.joinHyphen(tokenized);
    String expected = "my son 's friend , however , plays a high-risk game .";
    assertEquals(actual, expected);
  }

  /**
   * Test method for {@link joshua.decoder.io.DeNormalize#testJoinHyphen(java.lang.String)}.
   */
  @Test
  public void testJoinHypen_empty() throws Exception {
    String actual = DeNormalize.joinHyphen("");
    String expected = "";
    assertEquals(actual, expected);
  }

  /**
   * Test method for {@link joshua.decoder.io.DeNormalize#testJoinHyphen(java.lang.String)}.
   */
  @Test
  public void testJoinHyphen_1space_btw_2hyphens() throws Exception {
    String actual = DeNormalize.joinHyphen("a - - b");
    String expected = "a-- b";
    assertEquals(actual, expected);
  }

  /**
   * Test method for {@link joshua.decoder.io.DeNormalize#testJoinHyphen(java.lang.String)}.
   */
  @Test
  public void testJoinHyphen_2spaces_btw_2hyphens() throws Exception {
    String actual = DeNormalize.joinHyphen("a -  - b");
    String expected = "a--b";
    assertEquals(actual, expected);
  }

}
