import sys
import argparse
import abjad
import logging

from compositionManager import CompositionManager
import json

def setup_logging(log_level):
    """Set up logging configuration based on the provided log level."""
    numeric_level = getattr(logging, log_level.upper(), None)
    if not isinstance(numeric_level, int):
        raise ValueError(f'Invalid log level: {log_level}')
    logging.basicConfig(level=numeric_level, format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    return numeric_level

def load_config(config_filename):
    """Load and validate the configuration file."""
    try:
        with open(config_filename, 'r') as config_file:
            config = json.load(config_file)
        return config
    except FileNotFoundError:
        logging.error(f"Configuration file not found: {config_filename}")
        sys.exit(1)
    except json.JSONDecodeError:
        logging.error(f"Invalid JSON in configuration file: {config_filename}")
        sys.exit(1)

def main():
    parser = argparse.ArgumentParser(description="Generate music compositions based on configuration.")

    parser.add_argument(
        '--config',
        dest='config_filename',
        default='mobile_config.json',
        help='Path to the JSON configuration file'
    )

    parser.add_argument(
        '--lilypond-output',
        dest='lilypond_output',
        help='Path to output LilyPond file'
    )

    parser.add_argument(
        '--musicxml-output',
        dest='musicxml_output',
        help='Path to output MusicXML file'
    )

    parser.add_argument(
        '--loglevel',
        dest='log_level',
        choices=['debug', 'info', 'warning', 'critical'],
        type=str,
        default='warning',
        help='Set the logging level'
    )

    args = parser.parse_args()

    setup_logging(args.log_level)
    mobile_config = load_config(args.config_filename)
    numeric_log_level = getattr(logging, args.log_level.upper(), None)
    logger = logging.Logger(__name__, level=numeric_log_level)
    composition_length = 2000  # Default value, can be overridden by config if needed
    if "length" in mobile_config["composition"]:
        composition_length = int(mobile_config["composition"]["length"])
        logger.info(f'Length: {int(mobile_config["composition"]["length"])}')

    # Initialize CompositionManager and proceed with music generation
    composition_manager = CompositionManager(composition_config=mobile_config, lilypond_output_file=args.lilypond_output, musicxml_output_file=args.musicxml_output, length=int(composition_length), log_level=numeric_log_level)
    print(f'Composition length: {mobile_config["composition"]["length"]}')
    print(f'{mobile_config["rhythm_map"]}')
    print(f'{mobile_config["pitch_map"]}')
    print("================ Lines ================")
    for line in composition_manager.lines:
        logger.info(f"{line.pitch_class}")
        for note in line.notes:
            logger.info(f"\t {note}")
    print("================ Measures ================")
    for line in composition_manager.lines:
        logger.info(f"{line.pitch_class}")
        for measure in line.measures:
            logger.info(f"\t {measure}")

if __name__ == "__main__":
    main()
    # pc_array = [0, 11, 1, 2, 10, 8, 5, 3, 9, 4, 7, 6]
    # pc_array = [0, 1]
   # sys.exit(0)
    # sys.exit(0)
    ##print(comp_manager.score)
