type Language = 'de' | 'en';
type TranslationMap = Map<string, Record<Language, string>>;

const translations: TranslationMap = new Map();
let currentLang: Language = (localStorage.getItem('lang') as Language) || 'de';

export function setLanguage(lang: Language): void {
    localStorage.setItem('lang', lang);
    currentLang = lang;
}

export function getLanguage(): Language {
    return currentLang;
}

export async function loadTranslations(): Promise<void> {
    const response = await fetch('/assets/translations.csv');
    console.log(response)
    if (!response.ok) throw new Error('CSV not found');

    const csvText = await response.text();
    const lines = csvText.trim().split('\n');
    const [headerLine, ...dataLines] = lines;

    const headers = headerLine.split(';').map(h => h.trim());

    for (const line of dataLines) {
        const values = line.split(';').map(v => v.trim());
        const row: Record<string, string> = {};

        headers.forEach((header, i) => {
            row[header] = values[i] || '';
        });
        console.log('row:', row);

        if (row.key) {
            console.log('row:', row.key);
            translations.set(row.key, {
                de: row.de || '', en: row.en || '',
            });
        }
    }

    console.log('Translations loaded:', translations);
}

export function translate(key: string): string {
    return translations.get(key)?.[currentLang] || key;
}
