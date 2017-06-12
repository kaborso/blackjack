# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).


## [Unreleased] - 2017-06-11
### Added
- Support for parsing player mentions, making dealer decisions, and tweeting
- Support for game sessions, hands, hitting, standing, and tallying
- Helpers such as env, match?, and any?
- Storage abstraction with put, retrieve, archive, and delete functions
- Support for formatting game data into 140 characters and replying
- [core.async](https://github.com/clojure/core.async) to empower workers
- [carmine](https://github.com/ptaoussanis/carmine) for talking to Redis
- [twitter-api](https://github.com/adamwynne/twitter-api) for talking to Twitter

### Changed
- Decks and hands are vectors now

## [Unreleased] - 2017-05-22
### Changed
- Completed tests and code for generating a standard playing card deck

## [Unreleased] - 2017-05-21
### Added
- Files from Stuart Sierra's [reloaded](https://github.com/stuartsierra/reloaded) Leiningen template.
- Initial tests and code for generating a standard playing card deck

## [Unreleased] - 2017-05-19
### Added
- Basic project files (license, .gitignore, readme)
