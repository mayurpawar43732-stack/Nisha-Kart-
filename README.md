# Nisha Kart

An original, mobile-first Indian shopping storefront demo. It is inspired by familiar e-commerce shopping patterns, while using original Nisha Kart branding and interface design.

## Included
- Responsive storefront with category navigation, product search, sorting and deal filters
- Product cards, wishlist and shopping cart
- Demo checkout with Indian address fields and INR pricing
- Local order history and customer profile
- Local-only admin demo to add products and export demo data
- Installable PWA with a basic offline app shell
- Capacitor configuration for packaging the web app as an Android project
- GitHub Actions workflow for GitHub Pages deployment

## Run locally
Open `index.html` in a browser, or serve the folder with any static web server.

## Publish on GitHub Pages
1. Open repository **Settings → Pages**.
2. Set the source to **GitHub Actions**.
3. Push to the `main` branch. The included workflow deploys the site.
4. Your site will be available at `https://mayurpawar43732-stack.github.io/Nisha-Kart-/` after the workflow succeeds.

## Android packaging
Install Node.js and Android Studio on a computer, then run:
```bash
npm install
npx cap add android
npx cap sync android
npx cap open android
```
Build the APK from Android Studio. The repository contains the web app and Capacitor setup; it does not contain a prebuilt APK.

## Important prototype limitations
This version deliberately has **no Firebase and no backend**. Cart, profile, wishlist, products added in the admin demo, and orders are stored in the current browser's localStorage. They do not sync between devices or users. The admin demo is not secure and must not be used as a real admin panel. Payment choices are labels only: no UPI, card, payment gateway, shipping, authentication, seller operations, or live order tracking is connected. Do not accept real customer payments or orders using this prototype.

The app uses no Amazon or Flipkart logos, assets, or proprietary code. Product imagery is represented by emoji placeholders and can be replaced with licensed product photos.
