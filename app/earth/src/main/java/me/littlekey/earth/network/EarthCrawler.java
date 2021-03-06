package me.littlekey.earth.network;

import android.net.Uri;
import android.text.TextUtils;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.littlekey.earth.model.Model;
import me.littlekey.earth.model.proto.Art;
import me.littlekey.earth.model.proto.Comment;
import me.littlekey.earth.model.proto.Count;
import me.littlekey.earth.model.proto.Fav;
import me.littlekey.earth.model.proto.Image;
import me.littlekey.earth.model.proto.Picture;
import me.littlekey.earth.model.proto.Tag;
import me.littlekey.earth.utils.Const;
import me.littlekey.earth.utils.EarthUtils;

/**
 * Created by littlekey on 16/6/17.
 */
public class EarthCrawler {

  public static Art createArtItemFromElement(Element element) throws Exception {
    Elements children = element.children();
    // NOTE : find category
    Element categoryEle = children.get(0);
    Model.Category category = Model.Category.from(categoryEle.select("img.ic").attr("alt"));
    // NOTE : find date
    Element dateEle = children.get(1);
    String date = dateEle.text();
    // NOTE : find cover and title and url and rating
    Element artEle = children.get(2);
    String cover =  artEle.select("div.it2 > img").attr("src");
    if (TextUtils.isEmpty(cover)) {
      Pattern coverPattern = Pattern.compile("inits~(.*?)~(.*?)/(.*?)~");
      Matcher coverMatcher = coverPattern.matcher(artEle.select("div.it2").text());
      if (coverMatcher.find()) {
        cover = String.format("http://%s/%s/%s",
            coverMatcher.group(1), coverMatcher.group(2), coverMatcher.group(3));
      }
    }
    Elements titleAndUrlElements = artEle.select("div.it5 > a");
    String artUrl  = titleAndUrlElements.attr("href");
    List<String> paths = Uri.parse(artUrl).getPathSegments();
    String gallery_id = paths.get(1);
    String token = paths.get(2);
    String title = titleAndUrlElements.text();
    Pattern ratingPattern = Pattern.compile("background-position:\\s*?-?(\\d*?)px\\s*?-?(\\d*?)px;");
    Matcher ratingMatcher = ratingPattern.matcher(artEle.select("div.it4 > div").attr("style"));
    int ratingNum = 0;
    float ratingOffset = 0f;
    if (ratingMatcher.find()) {
      ratingNum = 5 - Integer.valueOf(ratingMatcher.group(1)) / 16;
      ratingOffset = Integer.valueOf(ratingMatcher.group(2)) == 1 ? 0.0f : 0.5f;
    }
    float rating = ratingNum - ratingOffset;
    // NOTE : find publisher
    Element publisherEle = children.get(3);
    Elements nameAndUrlElements = publisherEle.select("div > a");
//    String publisherUrl = nameAndUrlElements.attr("href");
    String publisherName = nameAndUrlElements.text();

    return new Art.Builder()
        .title(title)
        .gid(gallery_id)
        .token(token)
        .publisher_name(publisherName)
        .category(category.getValue())
        .rating(rating)
        .cover(cover)
        .date(date)
        .url(artUrl)
        .build();
  }

  public static Art createArtDetailFromElements(Elements elements, String gid, String token)
      throws Exception {
    // NOTE : find cover
    String cover = elements.select("#gd1 > img").attr("src");
    // NOTE : find title
    String title = elements.select("#gn").text();
    // NOTE : find category
    Model.Category category = Model.Category.from(elements.select("#gdc > a > img").attr("alt"));
    // NOTE : find publisher name
    String publisherName = elements.select("#gdn > a").text();
    // NOTE : find date, language, file size, pages, likes
    Elements detailElements = elements.select("#gdd > table > tbody > tr");
    String date = detailElements.get(0).select(".gdt2").text();
    String language = EarthUtils.strip(detailElements.get(3).select(".gdt2").text());
    String fileSize = detailElements.get(4).select(".gdt2").text();
    Pattern pagesPattern = Pattern.compile("^(\\d*?)\\s*pages$");
    Matcher pagesMatcher = pagesPattern.matcher(EarthUtils.strip(detailElements.get(5).select(".gdt2").text()));
    int pages = 0;
    if (pagesMatcher.find()) {
      pages = Integer.valueOf(pagesMatcher.group(1));
    }
    Pattern likesPattern = Pattern.compile("^(\\d*?)\\s*times$");
    Matcher likesMatcher = likesPattern.matcher(EarthUtils.strip(detailElements.get(6).select(".gdt2").text()));
    int likes = 0;
    if (likesMatcher.find()) {
      likes = Integer.valueOf(likesMatcher.group(1));
    }
    // NOTE : find rating and how many counts
    int rating_count = Integer.valueOf(elements.select("#rating_count").text());
    Pattern ratingPattern = Pattern.compile("^Average:\\s*([\\d\\.]*?)$");
    Matcher ratingMatcher = ratingPattern.matcher(EarthUtils.strip(elements.select("#rating_label").text()));
    float rating = 0f;
    if (ratingMatcher.find()) {
      rating = Float.valueOf(ratingMatcher.group(1));
    }
    boolean liked = elements.select("#fav > div").size() != 0;
    List<Tag> tags = new ArrayList<>();
    Elements tagsElements = elements.select("#taglist > table > tbody > tr");
    for (Element tagEle: tagsElements) {
      String key = tagEle.select("td.tc").text();
      List<Tag> values = new ArrayList<>();
      for (Element valueEle: tagEle.select("td > div > a")) {
        values.add(new Tag.Builder()
            .id(UUID.randomUUID().toString())
            .key(valueEle.text())
            .url(valueEle.attr("href"))
            .build());
      }
      tags.add(new Tag.Builder()
          .id(UUID.randomUUID().toString())
          .key(TextUtils.substring(key, 0, key.length() - 1)) // NOTE : remove colon
          .values(values)
          .build());
    }
    return new Art.Builder()
        .title(title)
        .gid(gid)
        .token(token)
        .cover(cover)
        .date(date)
        .file_size(fileSize)
        .language(language)
        .category(category.getValue())
        .publisher_name(publisherName)
        .pages(pages)
        .likes(likes)
        .rating_count(rating_count)
        .rating(rating)
        .tags(tags)
        .liked(liked)
        .url(Uri.parse(Const.API_ROOT).buildUpon()
            .appendEncodedPath("g")
            .appendEncodedPath(gid)
            .appendEncodedPath(token)
            .build().toString())
        .build();
  }

  public static Image createImageFromElement(Element element, int pages, String gToken) throws Exception {
    Image.Builder builder = new Image.Builder().pages(pages).gallery_token(gToken);
    Pattern pattern = Pattern.compile("^(.*?):(.*?)$");
    if (element.select(".gdtl").size() != 0) {
      builder.is_thumbnail(false);
      Element img = element.select("a > img").get(0);
      builder.src(img.attr("src"));
      for (String style: img.attr("style").split("; ")) {
        Matcher matcher = pattern.matcher(style);
        if (matcher.find()) {
          String value = matcher.group(2);
          switch (matcher.group(1).toLowerCase()) {
            case "width":
              builder.width(Integer.valueOf(value.substring(0, value.length() - 2)));
              break;
            case "height":
              builder.height(Integer.valueOf(value.substring(0, value.length() - 2)));
              break;
          }
        }
      }
    } else if (element.select(".gdtm > div").size() != 0) {
      builder.is_thumbnail(true);
      Element img = element.select(".gdtm > div").get(0);
      for (String style : img.attr("style").split("; ")) {
        Matcher matcher = pattern.matcher(style);
        if (matcher.find()) {
          String value = matcher.group(2);
          switch (matcher.group(1).toLowerCase()) {
            case "width":
              builder.width(Integer.valueOf(value.substring(0, value.length() - 2)));
              break;
            case "height":
              builder.height(Integer.valueOf(value.substring(0, value.length() - 2)));
              break;
            case "background":
              String[] backgroundParams = value.split(" ");
              builder.src(backgroundParams[1].substring(4, backgroundParams[1].length() - 1));
              builder.offset(Integer.valueOf(
                  backgroundParams[2].substring(0, backgroundParams[2].length() - 2)));
              break;
          }
        }
      }
    } else {
      return null;
    }
    builder.origin_url(element.select("a").attr("href"));
    builder.number(Integer.valueOf(element.select("a > img").attr("alt")));
    return builder.build();
  }

  public static Count createPageCountFromElements(Elements elements) throws Exception {
    if (elements.size() == 0) {
      return null;
    }
    int currentPage = Integer.valueOf(elements.select("td.ptds > a").text());
    int totalPage = Integer.valueOf(elements.get(elements.size() - 2).select("a").text());

    return new Count.Builder()
        .number(currentPage)
        .pages(totalPage)
        .build();
  }

  public static Fav createFavFromElement(Element element, String apply) throws Exception {
    String id = element.select("div > input").attr("value");
    Elements elements = element.select("div");
    String name = elements.get(elements.size() - 2).text();
    return new Fav.Builder()
        .id(id)
        .name(name)
        .apply(apply)
        .build();
  }

  public static Comment createCommentFromElement(Element element) throws Exception {
    Element dateAndAuthorEle = element.select("div.c2 > div.c3").get(0);
    Pattern datePattern = Pattern.compile("^Posted on (.*?) UTC by:   (.*?)$");
    Matcher dateMatcher = datePattern.matcher(dateAndAuthorEle.text());
    String date = null;
    String author = null;
    if (dateMatcher.find()) {
      date = dateMatcher.group(1);
      author = dateMatcher.group(2);
    }
    Elements scoreElements = element.select("div.c2 > div.c5");
    int score = 0;
    if (scoreElements.size() > 0) {
      Pattern scorePattern = Pattern.compile("^Score +(.*?)$");
      Matcher scoreMatcher = scorePattern.matcher(scoreElements.get(0).text());
      if (scoreMatcher.find()) {
        score = Integer.valueOf(scoreMatcher.group(1));
      }
    }
    String content = element.select("div.c6").html();
    return new Comment.Builder()
        .score(score)
        .date(date)
        .author(author)
        .content(content)
        .build();
  }

  public static Picture createPictureFromElements(Elements elements) throws Exception {
    String src = elements.select("#img").attr("src");
    Elements countElements = elements.select("#i2 > div.sn > div > span");
    int current_page = Integer.valueOf(countElements.get(0).text());
    int total_page = Integer.valueOf(countElements.get(1).text());
    String name = elements.select("#i1 > h1").text();
    String[] imgDetails = elements.select("#i2 > div:nth-child(2)").text().split(" :: ");
    @SuppressWarnings("unused")
    String filename = imgDetails[0];
    String[] resolution = imgDetails[1].split(" x ");
    @SuppressWarnings("unused")
    String size = imgDetails[2];
    int width = Integer.valueOf(resolution[0]);
    int height = Integer.valueOf(resolution[1]);
    Image image = new Image.Builder()
        .number(current_page)
        .pages(total_page)
        .width(width)
        .height(height)
        .is_thumbnail(false)
        .src(src)
        .build();
    return new Picture.Builder()
        .image(image)
        .name(name)
        .build();
  }
}
