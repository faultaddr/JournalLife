# Notebook Journal - Specification Document (SPEC.md)

## 1. Project Overview

### 1.1 Product Name
**Notebook Journal**: Cross-platform journal/note-taking application

### 1.2 Target Platforms
- Android
- iOS
- macOS
Technology Stack: **Compose Multiplatform** (UI), Kotlin Multiplatform (Business/Data Layer)

### 1.3 Core Objectives
Easily edit content in a "journal" format across multiple devices, supporting:
- Mixed content elements (text, images, etc.) layout
- Export (text/image/collection)
- Categorization (Books/chapters/directory structure)
- User management + Privacy management (public vs private journals)
- Data statistics and chart display

### 1.4 Terminology
- Journal: A content entity (similar to an article/a page of journal)
- Book: Collection of journals, supports chapter/directory structure
- Block: Smallest editing unit in a journal, such as text block, image block, todo block, etc.
- Visibility: Public (external)/Private (internal)
- Workspace: User's content space under the same account (optional, for team/multi-space expansion)

## 2. Users and Use Cases

### 2.1 Target Users
- Individuals: Daily records, study notes, travel journals
- Content creators: Want to organize content into "books/collections" for sharing
- Data trackers: Want to track habits, moods, reading, expenses, etc. and generate charts

### 2.2 Key Use Cases (User Stories)
1. I can quickly insert text and images like writing a journal and adjust formatting.
2. I can export a journal as an image or text file to share with others.
3. I can organize multiple journals into a "book" with table of contents, chapters, and cover.
4. I can set a journal as private (visible only to myself) or public (can be shared via link/visible to others).
5. I can see statistical charts by time/category (e.g., weekly writing frequency, mood trends).

## 3. Feature Scope

### 3.1 MVP (Must have in Phase 1)
- Account system (register/login/logout)
- Journal editor: Text + image mixing, basic formatting
- Classification: Books/directory structure (at least "Books -> Journal List")
- Privacy: Journal-level Public/Private
- Export: Export as image (long image)/export as plain text (Markdown or TXT)
- Statistics: Basic statistics (writing frequency, word count, image count) + 2-3 basic charts

### 3.2 Future Enhancements (Optional)
- Multi-user collaboration/shared books
- Offline-first + real-time cross-device sync conflict resolution
- Rich text enhancements (tables, code blocks, drawing, stickers)
- Template system (travel templates, study templates, etc.)
- Web display page for public content

## 4. Information Architecture and Data Model

### 4.1 Core Entities

#### User
- id: String
- email/phone: String (at least one)
- displayName: String
- avatarUrl: String?
- createdAt: Timestamp
- settings:
  - locale, theme
  - privacyDefaults (default public/private)

#### Book
- id: String
- ownerId: String
- title: String
- coverImageId: String?
- description: String?
- visibilityDefault: Enum(Public, Private) (default visibility for new journals in the book)
- createdAt, updatedAt

#### JournalEntry (Journal)
- id: String
- ownerId: String
- bookId: String
- title: String
- createdAt, updatedAt
- visibility: Enum(Public, Private)
- tags: [String]
- blocks: [Block] (see below)
- metricsCache:
  - wordCount: Int
  - imageCount: Int

#### Block (Element blocks, suggest using sealed class)
Common fields:
- id: String
- type: Enum(Text, Image, Todo, Divider, Quote, Heading, ...)
- createdAt, updatedAt
- orderIndex: Int (or use array order)

TextBlock:
- text: String
- style: { bold?, italic?, fontSize?, color?, alignment? } (MVP can simplify to few styles)
- format: Enum(Plain, MarkdownLite)

ImageBlock:
- imageId: String
- caption: String?
- layout: Enum(Inline, FullWidth)
- crop: { x,y,w,h }? (optional)

#### Media
- id: String
- ownerId: String
- type: Enum(Image)
- localPath: String? (offline cache)
- remoteUrl: String? (cloud storage)
- width, height
- createdAt

#### Share (Sharing/External Access Control)
- id: String
- targetType: Enum(JournalEntry, Book)
- targetId: String
- ownerId: String
- visibility: Enum(PublicLink, Disabled) (MVP: Public link only/Disabled)
- shareToken: String (random, unguessable)
- createdAt, expiredAt?

## 5. Detailed Functional Requirements

### 5.1 Account and User Management
#### 5.1.1 Registration/Login
- Support email registration/login (MVP)
- Support Apple/Google login (enhancement)
- Clear failure notifications (password error/account doesn't exist/network error)

#### 5.1.2 Session and Security
- Token login state saving (Keychain/Keystore)
- Support logout, clear local cache (optional)

**Acceptance Criteria**
- Users can log in to the same account on three platforms and see the same data (if cloud-based; if starting with local version then clearly indicate no sync)

### 5.2 Journal Editor (Core)
#### 5.2.1 Editing Experience
- Support creating/editing/deleting journals
- Journals consist of Blocks:
  - TextBlock: Input, line breaks, paragraphs
  - ImageBlock: Insert images from gallery/take photos (macOS: file selection)
- Block-level operations:
  - Insert (add text/image at current position)
  - Delete
  - Move up/down (drag-and-drop sorting or buttons)
- Auto-save:
  - Local instant save (debounce after N seconds of input)
  - Crash recovery (drafts/versions) (MVP can skip versions, at least prevent content loss)

#### 5.2.2 Basic Formatting (MVP)
- Title (Entry title)
- Text blocks support: Bold/italic/headings (select minimal set)
- Image blocks support: Full-width display + optional caption

**Acceptance Criteria**
- Can complete a journal with "text + 3 images" within 2 minutes without noticeable lag
- Content remains after app restart

### 5.3 Organization and "Bookification"
#### 5.3.1 Book Management
- Create/edit/delete Book
- Show journal list under Book (by time/custom sort)
- Move journals to other Books

#### 5.3.2 Directory/Chapters (Suggested: MVP Simplified)
- MVP: Book -> Entry list only
- Enhancement: Book -> Chapter -> Entry (chapters can be drag-and-drop sorted)

**Acceptance Criteria**
- Users can organize at least 30 journals into 3 books and quickly search/browse

### 5.4 Privacy and Internal/External Journals
#### 5.4.1 Private (Internal)
- Private: Visible only to logged-in user
- Default creation as Private (can be changed in settings)

#### 5.4.2 Public (External)
- Public: Can generate share links (with shareToken)
- After opening share link:
  - Show only public content of that entry or book
  - Don't show user's private information (email/internal tags, etc.)

#### 5.4.3 Permission Boundaries (MVP)
- Only "owner" can edit/delete
- Unauthenticated users can only view public content via link (if Web/in-app view provided)

**Acceptance Criteria**
- Private content cannot be accessed via any share link
- Public links are not enumerable (token is random and sufficiently long)

### 5.5 Export Capabilities (Text, Images)
#### 5.5.1 Export Formats
- Export journals as:
  1) **Long image** (render text+images in layout as single or multiple images)
  2) **Plain text**: TXT or Markdown (recommended Markdown)
- Export books as (enhancement):
  - PDF / ePub / Markdown zip

#### 5.5.2 Export Scope and Behavior
- Single export
- Batch export (enhancement)
- During export can choose:
  - Whether to include cover/title/date
  - Whether to compress images (quality: high/medium/low)

**Acceptance Criteria**
- On mobile can export long image with 10 images, export time is controllable (e.g., < 10s, specific depending on implementation)
- Exported Markdown can be opened in common editors and image references are correct (local files or packaged)

### 5.6 Statistics and Charts
#### 5.6.1 Metric Definitions (MVP)
- Writing frequency: Count of entries by day/week/month
- Word count: Characters/words in TextBlocks
- Image count: Number of ImageBlocks
- Tag statistics: Top N tag distribution

#### 5.6.2 Chart Types (MVP)
- Line chart: Writing frequency trend over time
- Bar chart: Words per week
- Pie chart/bar chart: Tag distribution

#### 5.6.3 Statistical Scope
- All Books
- Filter by individual Book
- Time range filter (last 7 days/last 30 days/custom)

**Acceptance Criteria**
- Statistics page opens smoothly with 1000 journal scale (can use cached metrics)

## 6. Non-Functional Requirements

### 6.1 Cross-Platform Consistency
- UI interaction style unified, but follow platform system behaviors:
  - iOS back gesture
  - Android back button
  - macOS menu/shortcuts (⌘S save, ⌘F search, etc., enhancement)

### 6.2 Performance
- List scrolling without lag (60fps target)
- Low editor input latency
- Image loading uses thumbnail/cache strategy

### 6.3 Offline and Sync (Need to clarify strategy)
Two optional approaches (for AI/team use):
- Approach A (MVP simplified): Local-first, no cross-device sync; connect to cloud later
- Approach B (Recommended long-term): Offline-first + cloud sync
  - Local database: SQLDelight/Room (KMP side use SQLDelight)
  - Sync: Based on change logs (op log) or last modified time (weak consistency)
  - Conflict: When same entry edited on multiple devices, prompt to choose version or merge by blocks (enhancement)

### 6.4 Security and Privacy
- Encrypt local sensitive data (at least tokens; enhancement: database encryption)
- Public sharing by default excludes EXIF location info (remove EXIF when exporting images, enhancement)
- Privacy policy and user data deletion (enhancement: account deletion/data export)

## 7. UI/Page List (For AI to generate interfaces/routes)

### 7.1 Pages
1. Login/Register page
2. Home page (book list)
3. Book detail page (journal list)
4. Journal editor page (Block editor)
5. Journal viewing page (read-only)
6. Statistics page (charts)
7. Settings page (default privacy, export options, etc.)

### 7.2 Key Interactions
- Create journal: Floating button/toolbar
- Insert image: Toolbar button
- Long press block: Move/delete/copy (enhancement)
- Share: Generate link/copy link
- Export: Select format -> Generate file -> System share panel

## 8. API/Storage Interface Constraints

### 8.1 Local Storage (Suggested)
- Tables: users (optional), books, entries, blocks, media, shares
- Indexes: entry(bookId, updatedAt), block(entryId, orderIndex), media(ownerId)

### 8.2 Cloud Interface (If needed)
Minimal API example (REST for illustration only):
- POST /auth/register, /auth/login
- GET/POST/PUT/DELETE /books
- GET/POST/PUT/DELETE /entries
- GET/POST/PUT/DELETE /entries/{id}/blocks
- POST /media/upload
- POST /shares (create share link)
- GET /public/{shareToken} (view public entry/book)

## 9. Acceptance Checklist

- Three platforms runnable, core flow usable: Login -> Create book -> Create journal -> Insert text/images -> Save -> Export -> View statistics
- Private/Public access control correct
- Exported files can be successfully shared via system share panel
- Statistical data consistent with actual content (word count/images count/frequency)

## 10. Outstanding Questions (Need Answers to Finalize Spec)

1. **Must cross-device sync be implemented?** (Required/Optional/Not now)
2. For public "external journals", do we need **Web access** or only in-app viewing?
3. For exported "images", is it **single long image** or can be multi-page? Should it include background templates (journal style)?
4. Should editor support: To-dos, emojis/stickers, drawing, stylus (Apple Pencil)?
5. Does "bookification" need chapters/directories/cover templates? Should it support PDF/ePub export?
6. What specific business for statistics: Do you want to track writing habits, or field-based records like finance/mood/exercise? (Determines data structure)

## 11. Answers to Outstanding Questions (Finalized Spec)

Based on typical requirements for a journal application, here are the answers to finalize the specification:

1. **Cross-device sync**: For MVP, implement local-first approach without sync. Sync functionality will be added in future phases (Approach A initially, transition to Approach B).

2. **Public journal access**: For MVP, public journals only need to be accessible within the app. Web access can be implemented as an enhancement later.

3. **Exported images**: Export as single long image with optional background templates (journal-style). This provides the most social media-friendly format.

4. **Editor features**: For MVP, focus on text and images only. To-dos, emojis, drawing, and stylus support can be added as enhancements.

5. **Bookification features**: For MVP, implement basic book structure (Book -> Journal list). Chapters, directories, and cover templates can be added later. PDF/ePub export is an enhancement feature.

6. **Statistics scope**: Focus on writing habits initially (frequency, word count, image count, tags). Field-based tracking (finance/mood/exercise) can be added in future iterations.

With these answers, the specification is now complete and ready for implementation.