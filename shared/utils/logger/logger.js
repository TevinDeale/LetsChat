// shared/utils/logger.js
const { createLogger, format, transports } = require('winston');
const { combine, timestamp, printf, colorize } = format;

// Define a custom log format
const customFormat = printf(({ level, message, timestamp }) => {
  return `${timestamp} ${level}: ${message}`;
});

const createLoggerInstance = (serviceName) => {
  return createLogger({
    level: 'info',
    format: combine(
      timestamp(),
      colorize(),
      customFormat
    ),
    transports: [
      new transports.Console(),
      new transports.File({ filename: `${serviceName}.log` })
    ],
    exceptionHandlers: [
      new transports.File({ filename: `${serviceName}-exceptions.log` })
    ]
  });
};

module.exports = { createLoggerInstance };
