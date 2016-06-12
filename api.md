e-hentai api
============

host `https://forums.e-hentai.org`

## login

**POST** `/index.php?act=Login&CODE=01`

#### Body

form-data

- **b** `d`

- **bt** ``

- **CookieDate** `1`

- **UserName** `username`

- **PassWord** `password`

- **ipb_login_submit** `Login!`

## exhentai

host `http://exhentai.org`

#### Header

`user_id` and `user_pass_hash` get from login response `headers.get('Set-Cookie')`

```
user_id = login_response_header['Set-Cookie'].findall("ipb_member_id=(.*?)")
user_pass_hash = login_response_header['Set-Cookie'].findall("ipb_pass_hash=(.*?)")
```

- **Cookie** `ipb_member_id={{ user_id }};ipb_pass_hash={{ user_pass_hash }}`

